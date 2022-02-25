package com.example.footprints.worker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.core.app.ActivityCompat
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.*
import com.google.common.util.concurrent.ListenableFuture

class LocationUpdateCallbackWorker(
    context: Context,
    params: WorkerParameters
) : ListenableWorker(context, params) {
    private lateinit var client: FusedLocationProviderClient

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture(getCallback())
    }

    @SuppressLint("MissingPermission")
    private fun getCallback(): CallbackToFutureAdapter.Resolver<Result> {
        return CallbackToFutureAdapter.Resolver<Result> {
            client = LocationServices.getFusedLocationProviderClient(applicationContext)
            client.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                Looper.myLooper()!!
            )

            ""
        }
    }

    /**
     * ロケーションの取得条件を作成
     *
     * @return ロケーションの取得条件
     */
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
            super.onLocationResult(p0)

        }
    }
}