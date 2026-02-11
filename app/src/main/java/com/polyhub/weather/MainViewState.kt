package com.polyhub.weather

import com.polyhub.weather.api.ApiResponse
import com.polyhub.weather.api.Weather

sealed class MainViewState {

    object Loading : MainViewState()

    data class Success(val weather: Weather) : MainViewState()

    data class Error(val message: String) : MainViewState()
}