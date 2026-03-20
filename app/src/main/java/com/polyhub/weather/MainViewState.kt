package com.polyhub.weather

import com.polyhub.weather.api.WeatherUI
import com.polyhub.weather.api.WeatherForecast

sealed class MainViewState {

    object Loading : MainViewState()

    data class Success(val weatherUI: WeatherUI, val weatherForecast: WeatherForecast) : MainViewState()

    data class Error(val message: String) : MainViewState()
}