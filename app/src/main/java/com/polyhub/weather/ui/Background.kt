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
    TimeOfDay.SUNRISE -> listOf(Color(0xFFFFDAA1), Color(0xFFB87C5E))
    TimeOfDay.SUNSET -> listOf(Color(0xFFFF8C5A), Color(0xFF4A2F6B))
    TimeOfDay.NIGHT -> listOf(Color(0xFF191E3A), Color(0xFF0B0E1F))
}

fun cloudyGradient(timeOfDay: TimeOfDay): List<Color> = when (timeOfDay) {
    TimeOfDay.DAY -> listOf(Color(0xFFB0BEC5), Color(0xFF607D8B))      // светло-серый → серо-голубой
    TimeOfDay.SUNRISE -> listOf(Color(0xFFCFD8DC), Color(0xFF90A4AE))  // тёплый серый → холодный серый
    TimeOfDay.SUNSET -> listOf(Color(0xFFB0BEC5), Color(0xFF546E7A))   // серый с сиреневым отливом → тёмный грифельный
    TimeOfDay.NIGHT -> listOf(Color(0xFF37474F), Color(0xFF263238))     // тёмно-серый → почти чёрный
}

fun rainyGradient(timeOfDay: TimeOfDay): List<Color> = when (timeOfDay) {
    TimeOfDay.DAY -> listOf(Color(0xFF4B6584), Color(0xFF2C3A47))       // сине-серый → тёмный сланец
    TimeOfDay.SUNRISE -> listOf(Color(0xFF6D8A9E), Color(0xFF3A4E5E))   // мягкий синий → глубокий синий
    TimeOfDay.SUNSET -> listOf(Color(0xFF5D6D7E), Color(0xFF2E3B4E))    // фиолетово-серый → тёмно-синий
    TimeOfDay.NIGHT -> listOf(Color(0xFF1E2A3A), Color(0xFF0F1A24))     // очень тёмный синий → чёрный
}

fun snowyGradient(timeOfDay: TimeOfDay): List<Color> = when (timeOfDay) {
    TimeOfDay.DAY -> listOf(Color(0xFFD6EAF8), Color(0xFF85C1E9))       // нежно-голубой → светло-синий
    TimeOfDay.SUNRISE -> listOf(Color(0xFFF2D7D5), Color(0xFFE8B4B0))   // розовато-персиковый → коралловый (отблески рассвета на снегу)
    TimeOfDay.SUNSET -> listOf(Color(0xFFE5C5D4), Color(0xFFB990A4))    // сиреневый → серо-розовый (закат)
    TimeOfDay.NIGHT -> listOf(Color(0xFF384B60), Color(0xFF1C2A3A))     // тёмно-синий с холодным оттенком → почти чёрный
}