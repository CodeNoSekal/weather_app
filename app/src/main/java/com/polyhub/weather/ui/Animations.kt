package com.polyhub.weather.ui

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
import com.polyhub.weather.api.Weather
import kotlin.math.sin
import kotlin.random.Random

data class SnowFlake(
    var x: Float,
    var y: Float,
    val speedX: Float,
    val speedY: Float,
    var alpha: Float,
    var radius: Float
)

@Composable
fun SnowAnimation(
    weather: Weather
) {
    val flakes = remember {
        SnapshotStateList<SnowFlake>().apply {
            addAll(
                List(50) {
                    SnowFlake(
                        x = Random.nextFloat(),
                        y = Random.nextFloat(),
                        speedX = Random.nextFloat() * 0.0001f - 0.00005f,
                        speedY = Random.nextFloat() * 0.00015f + 0.0004f,
                        alpha = Random.nextFloat() * 0.5f + 0.5f,
                        radius = Random.nextFloat() * 4f + 3f
                    )
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        while(true) {
            val frameTime = withFrameNanos { }

            flakes.forEachIndexed { index, flake ->
                val newX = flake.x + flake.speedX
                val newY = flake.y + flake.speedY
                flakes[index] = flake.copy(
                    x = if (newX > 1.01f) -0.01f else if (newX < -0.01f) 1.01f else newX,
                    y = if (newY > 1.01f) -0.01f else newY,
                )
            }
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        flakes.forEach { flake ->
            drawCircle(
                color = Color.White.copy(alpha = flake.alpha),
                radius = flake.radius,
                center = Offset(flake.x * size.width, flake.y * size.height)
            )
        }
    }
}