package com.polyhub.weather.api

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset


enum class WeatherType {
    CLEAR, CLOUDS, RAIN, SNOW, OTHER
}

enum class TimeOfDay {
    DAY, NIGHT
}

data class WeatherUI(
    val id: Int,
    val locationName: String,
    val temperature: String,
    val description: String,
    val weatherType: WeatherType,
    val date: LocalDate,
    val time: LocalTime,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val timeOfDay: TimeOfDay
)

fun ApiResponse.toUiModel(): WeatherUI {
    val weather = weatherState.first()

    val zoneOffset = ZoneOffset.ofTotalSeconds(timezone)

    val dateTime = dt.toLocalDateTime(zoneOffset)
    val currentTime = dateTime.toLocalTime()
    val sunrise = sys.sunrise.toLocalDateTime(zoneOffset).toLocalTime()
    val sunset = sys.sunset.toLocalDateTime(zoneOffset).toLocalTime()

    return WeatherUI(
        id = weather.id,
        locationName = city,
        temperature = main.temp.toInt().toString(),
        description = weather.desc.replaceFirstChar { it.uppercase() },
        weatherType = weather.main.toWeatherType(),
        date =  dateTime.toLocalDate(),
        time = dateTime.toLocalTime(),
        sunrise = sunrise,
        sunset = sunset,
        timeOfDay = currentTime.toTimeOfDay(sunrise, sunset)
    )

}

fun LocalTime.toTimeOfDay(
    sunrise: LocalTime,
    sunset: LocalTime
): TimeOfDay {
    return if (this in sunrise..sunset) {
        TimeOfDay.DAY
    } else {
        TimeOfDay.NIGHT
    }
}

fun String.toWeatherType(): WeatherType = when (this) {
    "Clear" -> WeatherType.CLEAR
    "Clouds" -> WeatherType.CLOUDS
    "Rain", "Drizzle", "Thunderstorm" -> WeatherType.RAIN
    "Snow" -> WeatherType.SNOW
    else -> WeatherType.OTHER
}

fun Long.toLocalDateTime(offset: ZoneOffset): java.time.OffsetDateTime {
    return java.time.Instant.ofEpochSecond(this).atOffset(offset)
}
