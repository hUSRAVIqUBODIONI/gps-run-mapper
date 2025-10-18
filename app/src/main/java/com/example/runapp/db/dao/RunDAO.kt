package com.example.runapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.runapp.db.entities.Run
import kotlinx.coroutines.flow.Flow


@Dao
interface RunDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("Select * from run Order by timestamp")
    fun getRunByDate() : Flow<List<Run>>

    @Query("Select * from run Order by avgSpeedInKMH")
    fun getRunByAvgSpeedInKMH() : Flow<List<Run>>

    @Query("Select * from run Order by distanceInMeters")
    fun getRunByDistance() : Flow<List<Run>>

    @Query("Select * from run Order by timeInMillis")
    fun getRunByTime() : Flow<List<Run>>

    @Query("Select * from run Order by caloriesBurned")
    fun getRunByCalories() : Flow<List<Run>>
}