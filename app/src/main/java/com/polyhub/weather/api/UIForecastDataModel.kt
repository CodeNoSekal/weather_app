package com.polyhub.weather.api

import java.time.LocalTime
import java.time.ZoneOffset

data class WeatherForecast(
    val forecast: List<ForecastItem>
)

data class ForecastItem(
    val time: LocalTime,
    val temperature: String
)

fun ApiForecastResponse.toUiModel(): WeatherForecast {

    val weatherItems: MutableList<ForecastItem> = mutableListOf()

    val zoneOffset = ZoneOffset.ofTotalSeconds(this.city.timezone)

    for (item in this.list){

        val instant = java.time.Instant.ofEpochSecond(item.dateTime)
        val dateTime = instant.atOffset(zoneOffset)

        val time = dateTime.toLocalTime()

        val temp = item.main.temp.toInt().toString()

        weatherItems.add(ForecastItem(time, temp))
    }

    val first = weatherItems.first()

    var target = 0

    for (i in 1..<weatherItems.size){
        if (weatherItems[i].time == first.time){
            target = i
            break
        }
    }


    return WeatherForecast(
        forecast = weatherItems.toList().take(target)
    )

}