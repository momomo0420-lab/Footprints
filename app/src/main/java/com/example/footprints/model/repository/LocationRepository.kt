package com.example.footprints.model.repository

import android.location.Location
import com.example.footprints.model.entity.MyLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    // API操作
    fun getCurrentLocation(listener: (Location) -> Unit)

    // DB操作
    suspend fun insert(location: Location, address: String)
    suspend fun deleteAll()
    fun loadAll(): Flow<List<MyLocation>>
    suspend fun loadLastAddress(): String
}