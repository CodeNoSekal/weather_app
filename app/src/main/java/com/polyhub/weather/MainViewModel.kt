package com.polyhub.weather

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polyhub.weather.api.Api
import com.polyhub.weather.api.ApiResponse
import com.polyhub.weather.api.Location
import com.polyhub.weather.api.LocationProvider
import com.polyhub.weather.api.RetrofitClient
import com.polyhub.weather.api.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStore: DataStore<Preferences>,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val api: Api = RetrofitClient.api

    private val _state = MutableStateFlow<MainViewState>(MainViewState.Loading)
    private val _isRefreshing = MutableStateFlow(false)

    val state: StateFlow<MainViewState> = _state
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    val hasRequestedPermission: StateFlow<Boolean> =
        dataStore.data
            .map { it[PreferencesKeys.HES_REQUESTETED_LOCATION_PERMISSION] ?: false }
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
                settings[PreferencesKeys.HES_REQUESTETED_LOCATION_PERMISSION] = requested
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
        viewModelScope.launch {
            _isRefreshing.value = true
            loadWeather(true)
            _isRefreshing.value = false
        }

    }

    private fun loadWeather() {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            loadWeather(false)
        }
    }

    private suspend fun loadWeather(isRefresh: Boolean) {
        try {
            val location = getLocation()

            if (location != null) {
                val apiResponse: ApiResponse =
                    api.getWeather(location.latitude, location.longitude)
                _state.value =
                    MainViewState.Success(apiResponse.toUiModel())
            } else{
                _state.value =
                    MainViewState.Error("Не удалось определить местоположение")
            }
        } catch (e: Exception){
            Log.e("MainViewModel", "Error loading weather data", e)
            if (!isRefresh) {
                _state.value =
                    MainViewState.Error(e.message ?: "Unknown error")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLocation() : Location?{
        return locationProvider.getLastLocation()
    }

}