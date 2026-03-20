package com.polyhub.weather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.polyhub.weather.api.ForecastUI
import com.polyhub.weather.api.LocationUI
import com.polyhub.weather.api.WeatherUI
import com.polyhub.weather.api.WeatherType

@Composable
fun Screen(
    weather: WeatherUI,
    forecast: ForecastUI,
    location: LocationUI,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onMenuClick: () -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

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
                    WeatherTopBar(
                        locationName = location.name,
                        onMenuClick = onMenuClick
                    )
                },

                modifier = Modifier
                    .fillMaxSize()
            ) { innerPadding ->
                Content(
                    modifier = Modifier
                        .padding(innerPadding),
                    weatherUI = weather,
                    forecast = forecast
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopBar(
    locationName: String,
    onMenuClick: () -> Unit
){
    TopAppBar(
        title = {
            Text(
                text = locationName,
                color = MaterialTheme.colorScheme.primary
            )

        },
        navigationIcon = {
            IconButton(onClick = {
                onMenuClick()
            }) {
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
}

@Composable
fun WeatherAnimationLayer(
    weatherUI: WeatherUI
) {
    when (weatherUI.weatherType) {
        WeatherType.SNOW -> SnowAnimation(weatherUI)
        else -> Unit
    }
}