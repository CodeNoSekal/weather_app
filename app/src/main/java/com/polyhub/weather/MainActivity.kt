package com.polyhub.weather

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import com.polyhub.weather.api.Weather
import com.polyhub.weather.api.WeatherType
import com.polyhub.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.flow.map


class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext.dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            WeatherTheme {

                LocationPermission(viewModel)

                val state by viewModel.state.collectAsState()

                when(state){
                    is MainViewState.Loading -> {
                        Text("LOADING...")
                    }
                    is MainViewState.Success -> {
                        val weather = (state as MainViewState.Success).weather

                        Screen(
                            weather,
                            viewModel
                        )
                    }
                    is MainViewState.Error -> {
                        Text("ERROR")
                    }
                }
            }
        }
    }
}

@Composable
fun LocationPermission(
    viewModel: MainViewModel
){
    val context = LocalContext.current
    val dataStore = context.dataStore
    val hasRequestedPermission by dataStore.data
        .map {it[PreferencesKeys.HES_REQUESTETED_LOCATION_PERMISSION] ?: false}
        .collectAsState(initial = false)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }

        viewModel.saveRequestedPermission(true)

        if (granted){
            viewModel.onLocationPermissionGranted()
        } else{
            viewModel.onPermissionDenied()
        }
    }

    LaunchedEffect(hasRequestedPermission) {
        if(!hasRequestedPermission) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    weather: Weather,
    viewModel: MainViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        WeatherBackground(weather)

        WeatherAnimationLayer(weather)

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
                        IconButton( onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        IconButton( onClick = {
                            viewModel.refreshWeather()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh",
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
                weather
            )
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

@Composable
fun Content(
    modifier: Modifier = Modifier,
    weather: Weather
){
    Box(
    ){
        Column(modifier = modifier) {
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