package com.example.footprints.di

import android.content.Context
import androidx.room.Room
import com.example.footprints.model.dao.MyLocationDao
import com.example.footprints.model.database.MyLocationDatabase
import com.example.footprints.model.repository.LocationRepository
import com.example.footprints.model.repository.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FootprintsModule {
    @Singleton
    @Provides
    fun provideMyLocationDatabase(
        @ApplicationContext context: Context
    ): MyLocationDatabase {
        return Room.databaseBuilder(
            context,
            MyLocationDatabase::class.java,
            "my_location.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMyLocationDao(db: MyLocationDatabase): MyLocationDao {
        return db.myLocationDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ) : LocationRepository
}
