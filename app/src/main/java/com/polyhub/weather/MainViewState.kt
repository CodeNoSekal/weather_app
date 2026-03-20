package com.polyhub.weather

import com.polyhub.weather.api.ForecastUI
import com.polyhub.weather.api.UIData
import com.polyhub.weather.api.WeatherUI

sealed class MainViewState {

    object Loading : MainViewState()

    data class Success(val uiData: UIData) : MainViewState()

    data class Error(val message: String) : MainViewState()
}