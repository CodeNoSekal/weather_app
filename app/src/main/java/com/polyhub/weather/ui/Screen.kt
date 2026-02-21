package com.polyhub.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.polyhub.weather.MainViewModel
import com.polyhub.weather.MainViewState
import com.polyhub.weather.api.Weather
import com.polyhub.weather.api.WeatherType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    weather: Weather,
    viewModel: MainViewModel
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    val onRefresh = { viewModel.refreshWeather() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        WeatherBackground(weather)

        WeatherAnimationLayer(weather)

        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ) {

            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = weather.locationName,
                                color = MaterialTheme.colorScheme.primary
                            )

                        },
                        navigationIcon = {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                },

                modifier = Modifier
                    .fillMaxSize()
            ) { innerPadding ->
                Content(
                    modifier = Modifier
                        .padding(innerPadding),
                    weather = weather
                )
            }
        }
    }

}


@Composable
fun WeatherBackground(
    weather: Weather
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            when (weather.weatherType) {
                WeatherType.CLEAR -> Color(0xFF64B5F6)
                WeatherType.CLOUDS -> Color(0xFF90A4AE)
                WeatherType.RAIN -> Color(0xFF263238)
                WeatherType.SNOW -> Color(0xFF0D47A1)
                else -> Color.Gray
            }
        ))
}

@Composable
fun WeatherAnimationLayer(
    weather: Weather
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (weather.weatherType) {
            WeatherType.SNOW -> SnowAnimation(weather)
            else -> {}
        }
    }
}