package com.example.footprints.model.repository

import android.location.Location
import com.example.footprints.model.entity.MyLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun startLocationUpdate(listener: (Location) -> Unit)
    fun stopLocationUpdate()
    fun convertLocationToAddress(location: Location): String

    suspend fun insert(location: Location, address: String)

    fun loadAll(): Flow<List<MyLocation>>
    suspend fun loadLastLocation(): String
}