package com.example.footprints.model.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MyLocationClientImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MyLocationClient {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var listener: (Location) -> Unit

    override fun startLocationUpdate(listener: (Location) -> Unit) {
        this.listener = listener

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = createLocationRequest()

        val checkResult = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (checkResult != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun createLocationRequest() : LocationRequest {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        return locationRequest
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            listener(p0.locations[0])
        }
    }

    override fun convertLocationToAddress(location: Location) : String {
        if(!Geocoder.isPresent()) {
            return ""
        }

        val geocoder = Geocoder(context)
        val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        return addressList[0].getAddressLine(0).toString()
    }

    override fun stopLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}