package com.example.footprints.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.footprints.model.entity.MyLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface MyLocationDao {
    @Insert
    suspend fun insert(myLocation: MyLocation)

    @Query("select * from my_location order by date_and_time desc")
    fun loadAll(): Flow<List<MyLocation>>

    @Query("select address from my_location order by date_and_time desc limit 1")
    suspend fun loadLastAddress(): String
}