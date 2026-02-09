package com.polyhub.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val api: Api = RetrofitClient.api

    private val _state = MutableStateFlow<MainViewState>(MainViewState.Loading)

    val state: StateFlow<MainViewState> = _state

    init {
        refresh()
    }



    private fun refresh(){
        val latitude: String? = null
        val longitude: String? = null
        val city: String? = null

        viewModelScope.launch { getLocation() }

    }

    private suspend fun getLocation(){

    }

    private fun loadWeatherData(
        latitude: String,
        longitude: String,
        city: String

    ) {
        viewModelScope.launch {
            try {
                _state.value = MainViewState.Loading

                val apiResponse: ApiResponse = api.getWeather()

                _state.value = MainViewState.Success(apiResponse)
            } catch (e: Exception){
                Log.e("MainViewModel", "Error loading weather data", e)
                _state.value = MainViewState.Error(e.message ?: "Unknown error")
            }
        }
    }
}