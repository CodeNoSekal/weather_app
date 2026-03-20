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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.polyhub.weather.api.LocationProvider
import com.polyhub.weather.ui.LoadingScreen
import com.polyhub.weather.ui.LocationsScreen
import com.polyhub.weather.ui.Screen
import com.polyhub.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            applicationContext.dataStore,
            LocationProvider(context = applicationContext),
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "savedCities"
            ).build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                App(viewModel)
            }
        }
    }
}

@Composable
fun App(viewModel: MainViewModel){

    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()

    LocationPermission(viewModel)

    NavHost(
        navController = navController,
        startDestination = "weather_screen"
    ) {
        composable("weather_screen") {
            WeatherRoute(state, viewModel, navController)
        }
        composable("locations_screen") {
            LocationsRoute(state, viewModel, navController)
        }
        composable("search") {
            CitySearch(viewModel, navController)
        }
    }
}

@Composable
fun WeatherRoute(
    state: MainViewState,
    viewModel: MainViewModel,
    navController: NavController
){
    MainStateHandler(state) {success ->
        Screen(
            weather = success.uiData.weatherUI,
            forecast = success.uiData.forecastUI,
            location = success.uiData.locationUI,
            isRefreshing = viewModel.isRefreshing.collectAsState().value,
            onRefresh = { viewModel.refreshWeather() },
            onMenuClick = { navController.navigate("locations_screen") }
        )
    }
}

@Composable
fun LocationsRoute(
    state: MainViewState,
    viewModel: MainViewModel,
    navController: NavController
) {
    MainStateHandler(state) { success ->
        LocationsScreen(
            success.uiData.weatherUI,
            viewModel,
            navController
        )
    }
}

@Composable
fun CitySearch(
    viewModel: MainViewModel,
    navController: NavController
) {

}

@Composable
fun MainStateHandler(
    state: MainViewState,
    onSuccess: @Composable (MainViewState.Success) -> Unit
) {
    when(state){
        is MainViewState.Loading -> LoadingScreen()
        is MainViewState.Error -> Text("ERROR")
        is MainViewState.Success -> onSuccess(state)
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


