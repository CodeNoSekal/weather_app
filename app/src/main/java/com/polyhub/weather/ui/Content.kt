package com.polyhub.weather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polyhub.weather.R
import com.polyhub.weather.api.Weather

@Composable
fun Content(
    modifier: Modifier = Modifier,
    weather: Weather
){
    Box(
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MainContent(weather)
        }
    }
}

@Composable
fun MainContent(
    weather: Weather
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row() {
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
    }
}