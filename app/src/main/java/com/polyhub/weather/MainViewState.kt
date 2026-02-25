package com.polyhub.weather

import com.polyhub.weather.api.ApiResponse
import com.polyhub.weather.api.Weather
import com.polyhub.weather.api.WeatherForecast

sealed class MainViewState {

    object Loading : MainViewState()

    data class Success(val weather: Weather, val weatherForecast: WeatherForecast) : MainViewState()

    data class Error(val message: String) : MainViewState()
}