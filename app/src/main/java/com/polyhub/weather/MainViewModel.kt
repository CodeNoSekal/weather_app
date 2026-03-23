package com.polyhub.weather

import android.annotation.SuppressLint
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polyhub.weather.api.Api
import com.polyhub.weather.api.Location
import com.polyhub.weather.api.LocationProvider
import com.polyhub.weather.api.RetrofitClient
import com.polyhub.weather.api.UIData
import com.polyhub.weather.api.WeatherUI
import com.polyhub.weather.api.toUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(
    private val dataStore: DataStore<Preferences>,
    private val locationProvider: LocationProvider,
    database: AppDatabase
) : ViewModel() {

    private val api: Api = RetrofitClient.api

    private val _state = MutableStateFlow<MainViewState>(MainViewState.Loading)
    private val _isRefreshing = MutableStateFlow(false)
    private val _searchResults = MutableStateFlow<List<UIData>>(listOf())
    private val _response = MutableStateFlow<List<UIData>>(listOf())

    val state: StateFlow<MainViewState> = _state
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val searchResults: StateFlow<List<UIData>> = _searchResults
    val response: StateFlow<List<UIData>> = _response

    var cityID: UUID? = null

    val cityDao = database.cityDao()

    val hasRequestedPermission: StateFlow<Boolean> =
        dataStore.data
            .map { it[PreferencesKeys.HES_REQUESTED_LOCATION_PERMISSION] ?: false }
            .stateIn(
                scope = viewModelScope,
                started = (SharingStarted.WhileSubscribed(5_000)),
                initialValue = false
            )

    init {
        loadWeather()
        request()
    }

    fun saveRequestedPermission(requested: Boolean) {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[PreferencesKeys.HES_REQUESTED_LOCATION_PERMISSION] = requested
            }
        }
    }

    fun onLocationPermissionGranted(){
        loadWeather()
    }

    fun onPermissionDenied(){
        loadWeather()
    }

    fun refreshWeather() {

        if(_isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                var location: Location? = null

                if (cityID != null){
                    val city = cityDao.loadById(cityID!!)

                    location = Location(city.lat, city.lon)
                }else{
                    location = getLocation()
                }

                fetchWeather(location)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadWeather() {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            try {
                fetchWeather(getLocation())
            } catch (e: Exception){
                Log.e("MainViewModel", "Error loading weather data", e)
                _state.value =
                    MainViewState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun fetchWeather(location: Location?) {
        try {
            if (location == null) {
                _state.value = MainViewState.Error("Нет локации")
                return
            }

            _state.value =
                MainViewState.Success(loadWeatherData(location))

        } catch (e: Exception){
            Log.e("MainViewModel", "Error loading weather data", e)
            _state.value =
                MainViewState.Error(e.message ?: "Unknown error")
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLocation() : Location?{
        return locationProvider.getLastLocation()
    }

    private suspend fun loadWeatherData(
        location: Location,
        id: UUID? = null
    ) : UIData = coroutineScope {

        val weatherDeferred = async {
            api.getWeather(location.latitude, location.longitude)
        }

        val forecastDeferred = async {
            api.getForecast(location.latitude, location.longitude)
        }

        val locationNameDeferred = async {
            api.getLocationName(location.latitude, location.longitude)
        }

        val weather = weatherDeferred.await().toUiModel()
        val forecast = forecastDeferred.await().toUiModel()
        val location = locationNameDeferred.await().toUiModel()

        UIData(id = id, weatherUI = weather, forecastUI = forecast, locationUI = location)
    }


    fun findCity(query: String){
        viewModelScope.launch {
            fetchCity(query)
        }
    }


    private suspend fun fetchCity(query: String){
        try {
            val foundCities = api.findCity(query)

            val results = mutableListOf<UIData>()

            foundCities.forEach { city ->
                results.add(loadWeatherData(Location(city.latitude, city.longitude)))
            }

            _searchResults.value = results
        } catch (e: Exception){
            Log.e("MainViewModel", "Error searching cities", e)
        }
    }

    fun onCitySelected(city: UIData){
        viewModelScope.launch {

            val uuid = UUID.randomUUID()

            cityDao.insert(
                City(
                    id = uuid,
                    name = city.locationUI.name,
                    lat = city.locationUI.latitude,
                    lon = city.locationUI.longitude
                )
            )

            cityID = uuid

            refreshWeather()
        }
    }

    fun request(){
        viewModelScope.launch {
            try {
                val results = mutableListOf<UIData>()

                val location = getLocation()

                if (location != null){
                    results.add(loadWeatherData(location))
                }

                val cities = cityDao.getAll().first()

                cities.forEach { city ->
                    results.add(loadWeatherData(Location(city.lat, city.lon), city.id))
                }

                _response.value = results


            }catch (e: Exception){
                Log.e("MainViewModel", "Error loading weather data", e)
            }
        }
    }

    fun updateCityID(id: UUID?){
        cityID = id
        refreshWeather()
    }

}