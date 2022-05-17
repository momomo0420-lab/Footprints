package com.example.footprints.worker

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.footprints.model.repository.LocationRepository
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LocationUpdateWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: LocationRepository
) : ListenableWorker(context, params) {

    // コルーチンスコープ
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture {
            scope.launch {
                onLocationUpdate()
            }
        }
    }

    private suspend fun onLocationUpdate() {
//        repository.getCurrentLocation {
//            // 取得したロケーションをアドレス（文字列）へ変換
//            val geocoder = Geocoder(applicationContext)
//            val addressList = geocoder.getFromLocation(it.latitude, it.longitude, 1)
//            val address = addressList[0].getAddressLine(0).toString()
//
//            withContext(Dispatchers.IO) {
//                repository.insert()
//            }
//
//
//        }

    }
    /**
     * Test3
     */

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
//
//    interface LocationUpdateListener {
//        suspend fun onUpdate(location: Location)
//    }
}