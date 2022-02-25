package com.example.footprints.worker

import android.content.Context
import android.location.Location
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.footprints.model.repository.LocationRepository
import com.google.common.util.concurrent.ListenableFuture

class LocationUpdateWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: LocationRepository
) : ListenableWorker(context, params) {

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture {
            repository.getCurrentLocation(getLocationUpdateListener())

            ""
        }
    }

    private fun getLocationUpdateListener(): (Location) -> Unit {
        return object : (Location) -> Unit {
            override fun invoke(location: Location) {
//                val address = repository.convertLocationToAddress(location)
//                val lastAddress = repository.loadLastAddress()
//
//                if(address == lastAddress) {
//                    return
//                }
//                repository.insert(location, address)
            }
        }
    }

}