package com.polyhub.weather.api

import java.time.LocalTime
import java.time.ZoneOffset

data class ForecastUI(
    val forecast: List<ForecastUIItem>
)

data class ForecastUIItem(
    val time: LocalTime,
    val temperature: String
)

fun HourlyForecastResponse.toUiModel(): ForecastUI {
    val zoneOffset = ZoneOffset.ofTotalSeconds(timeData.timezone)

    val weatherItems = forecastList.map { item ->
        val dateTime = item.dateTime.toLocalDateTime(zoneOffset)

        ForecastUIItem(
            time = dateTime.toLocalTime(),
            temperature = item.weatherData.temp.toInt().toString()
        )
    }

    val firstTime = weatherItems.firstOrNull()?.time

    val targetIndex = weatherItems
        .drop(1)
        .indexOfFirst { it.time == firstTime }
        .let { if (it == -1) weatherItems.size else it + 1 }


    return ForecastUI(
        forecast = weatherItems.toList().take(targetIndex)
    )

}