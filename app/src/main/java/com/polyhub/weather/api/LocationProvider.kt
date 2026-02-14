package com.polyhub.weather.api

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await


data class Location(
    val latitude: String?,
    val longitude: String?
)

class LocationProvider(context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context.applicationContext)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLastLocation(): Location? {
        return try {
            val loc = fusedLocationClient.lastLocation.await()
            loc?.let { Location(it.latitude.toString(), it.longitude.toString()) }
        } catch (e: Exception) {
            null
        }
    }
}