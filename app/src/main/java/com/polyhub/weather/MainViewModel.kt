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
import com.polyhub.weather.api.toUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStore: DataStore<Preferences>,
    private val locationProvider: LocationProvider,
    database: AppDatabase
) : ViewModel() {

    private val api: Api = RetrofitClient.api

    private val _state = MutableStateFlow<MainViewState>(MainViewState.Loading)
    private val _isRefreshing = MutableStateFlow(false)

    val state: StateFlow<MainViewState> = _state
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

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
                fetchWeather()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadWeather() {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            fetchWeather()
        }
    }

    private suspend fun fetchWeather() {
        try {
            val location = getLocation()

            if (location == null) {
                _state.value = MainViewState.Error("Нет локации")
                return
            }

            val uiData = loadWeatherData(location)

            _state.value =
                MainViewState.Success(uiData)

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

    private suspend fun loadWeatherData(location: Location)
    : UIData = kotlinx.coroutines.coroutineScope {

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

        UIData(weather, forecast, location)
    }

}