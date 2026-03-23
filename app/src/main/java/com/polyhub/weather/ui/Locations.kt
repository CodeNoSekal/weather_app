package com.polyhub.weather.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.polyhub.weather.MainViewModel
import com.polyhub.weather.api.UIData
import com.polyhub.weather.api.WeatherUI
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    weatherUI: WeatherUI,
    response: List<UIData>,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    setID: (UUID?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        WeatherBackground(weatherUI)

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Погода",
                            color = MaterialTheme.colorScheme.primary
                        )

                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onSearchClick()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "search",
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
            LocationContent(
                modifier = Modifier
                    .padding(innerPadding),
                response = response,
                setID = setID
            )
        }
    }
}

@Composable
fun LocationContent(
    modifier: Modifier,
    response: List<UIData>,
    setID: (UUID?) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(response) {item ->
            LocationItem(
                item = item,
                modifier = Modifier.clickable {
                    setID(item.id)
                }
            )
        }
    }
}


@Composable
fun LocationItem(
    item: UIData,
    modifier: Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(item.locationUI.name)
    }

}