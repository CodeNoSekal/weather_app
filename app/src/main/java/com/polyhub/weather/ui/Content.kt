package com.polyhub.weather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polyhub.weather.R
import com.polyhub.weather.api.ForecastUI
import com.polyhub.weather.api.ForecastUIItem
import com.polyhub.weather.api.WeatherUI

@Composable
fun Content(
    modifier: Modifier = Modifier,
    weatherUI: WeatherUI,
    forecast: ForecastUI
){
    Box{
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MainContent(weatherUI, forecast)
        }
    }
}

@Composable
fun MainContent(
    weatherUI: WeatherUI,
    forecast: ForecastUI
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Text(
                text = weatherUI.temperature,
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
            text = weatherUI.description,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        Forecast(forecast)

    }
}


@Composable
fun Forecast(
    forecast: ForecastUI
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
            .padding(vertical = 12.dp)
    ){
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ){
            items(forecast.forecast) { item  ->
                ForecastItem(item)
            }
        }
    }
}

@Composable
fun ForecastItem(
    item: ForecastUIItem
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.time.toString(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Image(
            modifier = Modifier
                .width(35.dp)
                .height(35.dp)
                .padding(vertical = 4.dp)
            ,
            painter = painterResource(id = R.drawable.sun_icon),
            contentDescription = "sun"
        )
        Row{
            Text(
                text = item.temperature,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.degree),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}