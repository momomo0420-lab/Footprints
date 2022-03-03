package com.example.footprints.di

import android.content.Context
import android.location.Geocoder
import com.example.footprints.model.api.MyLocationClient
import com.example.footprints.model.api.MyLocationClientImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Singleton
    @Provides
    fun provideFusedLocationClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun provideMyGeocoder(
        @ApplicationContext context: Context
    ): Geocoder? {
        var geocoder: Geocoder? = null

        if(Geocoder.isPresent()) {
            geocoder = Geocoder(context)
        }

        return geocoder
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MyLocationClientModule {
    @Singleton
    @Binds
    abstract fun bindMyLocationClient(
        impl: MyLocationClientImpl
    ) : MyLocationClient
}