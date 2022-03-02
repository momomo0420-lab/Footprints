package com.example.footprints.worker

import android.content.Context
import android.location.Location
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

    // コルーチンスコープ
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture {
//            scope.launch {
//                repository.getCurrentLocation(getLocationUpdateListener())
//            }
        }
    }

//    private suspend fun getLocationUpdateListener(): (Location) -> Unit = {
//        withContext(Dispatchers.IO) {
//
//        }
//    }

//    private suspend fun getLocationUpdateListener(): LocationUpdateListener {
//        return object : LocationUpdateListener {
//            override suspend fun onUpdate(location: Location) {
//                withContext(Dispatchers.IO) {
//                    val address = repository.convertLocationToAddress(location)
//                    val lastAddress = repository.loadLastAddress()
//
//                    if(address == lastAddress) {
//                        return@withContext
//                    }
//                    repository.insert(location, address)
//                }
//            }
//        }
//    }

    interface LocationUpdateListener {
        suspend fun onUpdate(location: Location)
    }
}