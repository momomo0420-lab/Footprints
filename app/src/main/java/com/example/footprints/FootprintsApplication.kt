package com.example.footprints

import android.app.Application
import androidx.work.Configuration
import com.example.footprints.model.repository.LocationRepository
import com.example.footprints.worker.LocationUpdateWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FootprintsApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var repository: LocationRepository

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(LocationUpdateWorkerFactory(repository))
            .build()
    }
}