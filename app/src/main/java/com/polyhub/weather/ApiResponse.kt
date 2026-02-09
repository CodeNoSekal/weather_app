package com.polyhub.weather

data class ApiResponse(
    val weather: List<WeatherData>,
    val clouds: CloudsData,
    val dt: String,
    val name: String
)

data class WeatherData(
    val id: String,
    val main: String,
    val description: String
)

data class CloudsData(
    val all: String
)
