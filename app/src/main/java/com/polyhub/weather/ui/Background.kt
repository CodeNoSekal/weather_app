package com.polyhub.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.polyhub.weather.api.TimeOfDay
import com.polyhub.weather.api.Weather
import com.polyhub.weather.api.WeatherType

@Composable
fun WeatherBackground(
    weather: Weather
) {
    val colors = when(weather.weatherType){
        WeatherType.CLEAR -> clearGradient(weather.timeOfDay)
        WeatherType.CLOUDS -> cloudyGradient(weather.timeOfDay)
        WeatherType.RAIN -> rainyGradient(weather.timeOfDay)
        WeatherType.SNOW -> snowyGradient(weather.timeOfDay)
        WeatherType.OTHER -> cloudyGradient(weather.timeOfDay)
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = colors,
                startY = 0f,
                endY = Float.POSITIVE_INFINITY
            )
        )
    )
}

fun clearGradient(timeOfDay: TimeOfDay): List<Color> = when(timeOfDay){
    TimeOfDay.DAY -> listOf(Color(0xFF47B8FF), Color(0xFF315F8A))
    TimeOfDay.NIGHT -> listOf(Color(0xFF191E3A), Color(0xFF0B0E1F))
}

fun cloudyGradient(timeOfDay: TimeOfDay): List<Color> = when (timeOfDay) {
    TimeOfDay.DAY -> listOf(Color(0xFFB0BEC5), Color(0xFF607D8B))      // светло-серый → серо-голубой
    TimeOfDay.NIGHT -> listOf(Color(0xFF37474F), Color(0xFF263238))     // тёмно-серый → почти чёрный
}

fun rainyGradient(timeOfDay: TimeOfDay): List<Color> = when (timeOfDay) {
    TimeOfDay.DAY -> listOf(Color(0xFF4B6584), Color(0xFF2C3A47))       // сине-серый → тёмный сланец
    TimeOfDay.NIGHT -> listOf(Color(0xFF1E2A3A), Color(0xFF0F1A24))     // очень тёмный синий → чёрный
}

fun snowyGradient(timeOfDay: TimeOfDay): List<Color> = when (timeOfDay) {
    TimeOfDay.DAY -> listOf(Color(0xFFD6EAF8), Color(0xFF85C1E9))       // нежно-голубой → светло-синий
    TimeOfDay.NIGHT -> listOf(Color(0xFF384B60), Color(0xFF1C2A3A))     // тёмно-синий с холодным оттенком → почти чёрный
}