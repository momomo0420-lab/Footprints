package com.example.footprints.model.repository

import android.location.Location
import com.example.footprints.model.entity.MyLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun startLocationUpdate(listener: (Location) -> Unit)
    fun stopLocationUpdate()
    fun getCurrentLocation(listener: (Location) -> Unit)

    suspend fun insert(location: Location, address: String)
    suspend fun deleteAll()
    fun loadAll(): Flow<List<MyLocation>>
    suspend fun loadLastAddress(): String
}