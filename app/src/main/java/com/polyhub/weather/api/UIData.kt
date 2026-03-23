package com.polyhub.weather.api

import java.util.UUID

data class UIData(
    val id: UUID? = null,
    val weatherUI: WeatherUI,
    val forecastUI: ForecastUI,
    val locationUI: LocationUI
)
