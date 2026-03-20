package com.polyhub.weather.api

import com.squareup.moshi.Json

data class CurrentWeatherResponse(
    @param:Json(name = "weather")
    val weatherStates: List<WeatherState>,
    @param:Json(name = "main")
    val weatherData: MainWeatherData,
    val clouds: CloudsData,
    @param:Json(name = "dt")
    val rowDateTime: Long,
    @param:Json(name = "name")
    val locationName: String,
    @param:Json(name = "sys")
    val sunData: SunData,
    val timezone: Int
)

data class WeatherState(
    val id: Int,
    val main: String,
    @param:Json(name = "description")
    val rowDescription: String
)

data class MainWeatherData(
    val temp: Double
)

data class CloudsData(
    val all: String
)

data class SunData(
    val sunrise: Long,
    val sunset: Long
)

data class HourlyForecastResponse(
    @param:Json(name = "list")
    val forecastList: List<ForecastItem>,
    @param:Json(name = "city")
    val timeData: TimeData
)

data class ForecastItem(
    @param:Json(name = "dt")
    val dateTime: Long,
    @param:Json(name = "main")
    val weatherData: MainWeatherData
)

data class TimeData(
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)


data class LocationResponse(
    val name: String,
    @param:Json(name = "local_names")
    val localNames: LocalNames?,
    @param:Json(name = "lat")
    val latitude: Double,
    @param:Json(name = "lon")
    val longitude: Double
)

data class LocalNames(
    val ru: String?,
    val en: String?
)

