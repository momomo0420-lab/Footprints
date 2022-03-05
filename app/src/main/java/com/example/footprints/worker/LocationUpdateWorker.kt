package com.example.footprints.worker

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.footprints.model.repository.LocationRepository
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*

class LocationUpdateWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: LocationRepository
) : ListenableWorker(context, params) {
    companion object {
        private const val TAG = "LocationUpdateWorker"
        const val UNIQUE_WORK_NAME = "unique_work_name"
    }

    // コルーチンスコープ
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer ->
            Log.d(TAG, "Starting startWork()")
            repository.getCurrentLocation(
                getOnLocationUpdateListener(completer)
            )
        }
    }

    override fun onStopped() {
        super.onStopped()
        scope.cancel()
    }

    private fun getOnLocationUpdateListener(
        completer: CallbackToFutureAdapter.Completer<Result>
    ): (Location) -> Unit {
        return object : (Location) -> Unit {
            override fun invoke(location: Location) {
                val geocoder = Geocoder(applicationContext)
                val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val address = addressList[0].getAddressLine(0).toString()

                saveLocationWithAddress(location, address)

                completer.set(Result.success())
            }
        }
    }

    private fun saveLocationWithAddress(
        location: Location,
        address: String
    ) {
        scope.launch {
            repository.insert(location, address)
        }
    }
}