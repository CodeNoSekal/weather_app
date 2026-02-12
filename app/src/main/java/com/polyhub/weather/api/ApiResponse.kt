package com.polyhub.weather.api

import com.squareup.moshi.Json

data class ApiResponse(
    @param:Json(name = "weather")
    val weatherState: List<WeatherData>,
    val main: MainData,
    val clouds: CloudsData,
    val dt: Long,
    @param:Json(name = "name")
    val city: String,
    val sys: Sys,
    val timezone: Int
)

data class WeatherData(
    val id: Int,
    val main: String,
    @param:Json(name = "description")
    val desc: String
) {
    val description: String
        get() = desc.replaceFirstChar { it.uppercase() }
}

data class CloudsData(
    val all: String
)

data class MainData(
    val temp: Double
)

data class Sys(
    val sunrise: Long,
    val sunset: Long
)