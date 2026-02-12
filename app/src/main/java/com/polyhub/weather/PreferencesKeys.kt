package com.polyhub.weather

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object PreferencesKeys {
    val HES_REQUESTETED_LOCATION_PERMISSION = booleanPreferencesKey("has_requested_location_permission")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")