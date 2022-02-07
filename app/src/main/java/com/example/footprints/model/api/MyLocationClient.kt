package com.example.footprints.model.api

import android.location.Location

interface MyLocationClient {
    fun startLocationUpdate(listener: (Location) -> Unit)
    fun stopLocationUpdate()
    fun convertLocationToAddress(location: Location) : String
}