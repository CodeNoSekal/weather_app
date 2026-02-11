package com.polyhub.weather.api

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.time.Instant

data class Weather(
    val id: Int,
    val locationName: String,
    val temperature: String,
    val description: String,
    val intensity: Int,
    val weatherType: WeatherType,
    val date: LocalDate,
    val time: LocalTime,
    val sunrise: LocalTime,
    val sunset: LocalTime
)

enum class WeatherType {
    CLEAR, CLOUDS, RAIN, SNOW, OTHER
}

fun ApiResponse.toUiModel(): Weather {
    val weather = this.weatherState.first()
    val mainWeather = weather.main

    val weatherType = when(mainWeather) {
        "Clear" -> WeatherType.CLEAR
        "Clouds" -> WeatherType.CLOUDS
        "Rain" -> WeatherType.RAIN
        "Drizzle" -> WeatherType.RAIN
        "Thunderstorm" -> WeatherType.RAIN
        "Snow" -> WeatherType.SNOW
        else -> WeatherType.OTHER
    }

    val description = weather.desc.replaceFirstChar { it.uppercase() }

    val id = weather.id

    val zoneOffset = ZoneOffset.ofTotalSeconds(this.timezone)

    val instant = java.time.Instant.ofEpochSecond(this.dt)
    val dateTime = instant.atOffset(zoneOffset)

    val sunriseInstant = java.time.Instant.ofEpochSecond(this.sys.sunrise)
    val sunriseTime = sunriseInstant.atOffset(zoneOffset).toLocalTime()

    val sunsetInstant = java.time.Instant.ofEpochSecond(this.sys.sunset)
    val sunsetTime = sunsetInstant.atOffset(zoneOffset).toLocalTime()


    return Weather(
        id = id,
        locationName = this.city,
        temperature = "${this.main.temp.toInt()}",
        description = description,
        intensity = 1,
        weatherType = weatherType,
        date = dateTime.toLocalDate(),
        time = dateTime.toLocalTime(),
        sunrise = sunriseTime,
        sunset = sunsetTime
    )

}