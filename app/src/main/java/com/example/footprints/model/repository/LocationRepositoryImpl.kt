package com.example.footprints.model.repository

import android.content.Context
import android.location.Location
import com.example.footprints.model.api.MyLocationClient
import com.example.footprints.model.dao.MyLocationDao
import com.example.footprints.model.entity.MyLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: MyLocationClient,
    private val dao: MyLocationDao
) : LocationRepository {

    private lateinit var listener: (Location) -> Unit

    override fun startLocationUpdate(listener: (Location) -> Unit) {
        this.listener = listener

        client.startLocationUpdate { location ->
            listener(location)
        }
    }

    override fun stopLocationUpdate() {
        client.stopLocationUpdate()
    }

    override fun convertLocationToAddress(location: Location): String {
        return client.convertLocationToAddress(location)
    }

    override suspend fun insert(location: Location, address: String) {
        val now = System.currentTimeMillis()

        val myLocation = MyLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            address = address,
            dateAndTime = now
        )

        withContext(Dispatchers.IO) {
            dao.insert(myLocation)
        }
    }

    override fun loadAll(): Flow<List<MyLocation>> {
        return dao.loadAll()
    }

    override suspend fun loadLastLocation(): String {
        var address = ""
        withContext(Dispatchers.IO) {
            address = dao.loadLastLocation()
        }
        return address
    }
}