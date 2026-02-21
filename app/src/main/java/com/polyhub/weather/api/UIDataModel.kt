package com.polyhub.weather.api

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.Duration
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
    val sunset: LocalTime,
    val timeOfDay: TimeOfDay
)

enum class WeatherType {
    CLEAR, CLOUDS, RAIN, SNOW, OTHER
}

enum class TimeOfDay {
    DAY, NIGHT, SUNRISE, SUNSET
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

    val date = dateTime.toLocalDate()
    val time = dateTime.toLocalTime()

    val sunriseInstant = java.time.Instant.ofEpochSecond(this.sys.sunrise)
    val sunrise = sunriseInstant.atOffset(zoneOffset).toLocalTime()

    val sunsetInstant = java.time.Instant.ofEpochSecond(this.sys.sunset)
    val sunset = sunsetInstant.atOffset(zoneOffset).toLocalTime()

    val timeOfDay = getTimeOfDay(time, sunrise, sunset)

    return Weather(
        id = id,
        locationName = this.city,
        temperature = "${this.main.temp.toInt()}",
        description = description,
        intensity = 1,
        weatherType = weatherType,
        date = date,
        time = time,
        sunrise = sunrise,
        sunset = sunset,
        timeOfDay = timeOfDay
    )

}

fun getTimeOfDay(time: LocalTime, sunrise: LocalTime, sunset: LocalTime): TimeOfDay {
    // Проверяем рассвет
    if (Duration.between(sunrise, time).abs().toHours() <= 1) {
        return TimeOfDay.SUNRISE
    }

    // Проверяем закат
    if (Duration.between(sunset, time).abs().toHours() <= 1) {
        return TimeOfDay.SUNSET
    }

    // День: после рассвета +1 час и до заката -1 час
    if (time.isAfter(sunrise.plusHours(1)) && time.isBefore(sunset.minusHours(1))) {
        return TimeOfDay.DAY
    }

    // Всё остальное — ночь
    return TimeOfDay.NIGHT
}