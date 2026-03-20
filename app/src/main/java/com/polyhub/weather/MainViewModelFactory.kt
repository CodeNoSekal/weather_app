package com.polyhub.weather

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polyhub.weather.api.LocationProvider

class MainViewModelFactory(
    private val dataStore: DataStore<Preferences>,
    private val locationProvider: LocationProvider,
    private val database: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataStore, locationProvider, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}