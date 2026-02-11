package com.polyhub.weather

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.polyhub.weather.api.ApiResponse
import com.polyhub.weather.api.Weather
import kotlin.random.Random

data class RainDrop(
    var x: Float,
    var y: Float,
    val speed: Float
)

@Composable
fun SnowAnimation(
    weather: Weather
) {
    val flakes = remember {
        SnapshotStateList<RainDrop>().apply {
            addAll(
                List(50) {
                    RainDrop(
                        x = Random.nextFloat(),
                        y = Random.nextFloat(),
                        speed = Random.nextFloat() * 0.0005f + 0.002f
                    )
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        while(true) {
            val frameTime = withFrameNanos { }

            flakes.forEachIndexed { index, flake ->
                val newY = flake.y + flake.speed
                flakes[index] = flake.copy(
                    y = if (newY > 1f) 0f else newY
                )
            }
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        flakes.forEach { flake ->
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(
                    flake.x * size.width,
                    flake.y * size.height
                )
            )
        }
    }
}