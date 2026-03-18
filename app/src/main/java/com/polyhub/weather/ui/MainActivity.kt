package com.polyhub.weather.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.polyhub.weather.MainViewModel
import com.polyhub.weather.MainViewModelFactory
import com.polyhub.weather.MainViewState
import com.polyhub.weather.api.LocationProvider
import com.polyhub.weather.dataStore
import com.polyhub.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            applicationContext.dataStore,
            LocationProvider(context = applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            WeatherTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "weather_screen"
                ) {
                    composable("weather_screen") {
                        LocationPermission(viewModel)

                        val state by viewModel.state.collectAsState()

                        when(state){
                            is MainViewState.Loading -> {
                                LoadingScreen()
                            }
                            is MainViewState.Success -> {
                                val weather = (state as MainViewState.Success).weather
                                val forecast = (state as MainViewState.Success).weatherForecast

                                Screen(
                                    weather,
                                    viewModel,
                                    forecast,
                                    navController
                                )
                            }
                            is MainViewState.Error -> {
                                Text("ERROR")
                            }
                        }
                    }

                    composable("locations_screen") {

                        val state by viewModel.state.collectAsState()

                        LocationsScreen(
                            navController,

                        )
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
    val hasRequestedPermission by viewModel
        .hasRequestedPermission
        .collectAsState()

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


