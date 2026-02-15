package com.polyhub.weather

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.polyhub.weather.api.LocationProvider
import com.polyhub.weather.ui.Screen
import com.polyhub.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext.dataStore, LocationProvider(context = applicationContext))
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


