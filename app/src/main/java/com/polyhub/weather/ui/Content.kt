package com.polyhub.weather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polyhub.weather.R
import com.polyhub.weather.api.Weather
import com.polyhub.weather.api.WeatherForecast

@Composable
fun Content(
    modifier: Modifier = Modifier,
    weather: Weather,
    forecast: WeatherForecast
){
    Box{
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MainContent(weather, forecast)
        }
    }
}

@Composable
fun MainContent(
    weather: Weather,
    forecast: WeatherForecast
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Text(
                text = weather.temperature,
                fontSize = 72.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.degree),
                fontSize = 72.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = weather.description,
            color = MaterialTheme.colorScheme.primary
        )

        Forecast(forecast)

    }
}


@Composable
fun Forecast(
    forecast: WeatherForecast
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 140.dp)
            .padding(horizontal = 16.dp)
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
    ){
        LazyRow{
            items(forecast.forecast) { item  ->
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row{
                        Text(
                            text = item.temperature,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.degree),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = item.time.toString(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
