package com.example.footprints.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.footprints.model.dao.MyLocationDao
import com.example.footprints.model.entity.MyLocation

@Database(entities = [MyLocation::class], version = 1, exportSchema = false)
abstract class MyLocationDatabase : RoomDatabase() {
    abstract fun myLocationDao(): MyLocationDao
}