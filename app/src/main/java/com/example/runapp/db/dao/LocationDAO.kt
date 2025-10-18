package com.example.runapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.runapp.db.entities.Location
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationDAO {


    @Insert(onConflict = REPLACE)
    suspend fun insertLocation(location: Location)


    @Delete
    suspend fun deleteLocation(location: Location)


    @Query("Select * from locations")
    fun getAllLocations()  : Flow<List<Location>>

    @Query("Delete from locations")
    suspend fun deleteAllLocations()
}