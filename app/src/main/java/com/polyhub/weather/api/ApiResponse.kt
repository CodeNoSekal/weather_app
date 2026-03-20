package com.polyhub.weather.api

import com.polyhub.weather.City
import com.squareup.moshi.Json

data class WeatherResponse(
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
)

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

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: LocationData
)

data class ForecastItem(
    @param:Json(name = "dt")
    val dateTime: Long,
    val main: MainData
)

data class LocationData(
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class LocationResponse(
    val results: List<CityData>
)

data class CityData(
    val name: String,
    @param:Json(name = "local_names")
    val localNames: LocalNames?,
    val lat: String,
    val lon: String
)

data class LocalNames(
    val ru: String?,
    val en: String?
)

