package com.example.runapp.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "run")
data class Run(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val runImg : String = "",
    val timestamp : Long = 0L,
    val avgSpeedInKMH : Float = 0f,
    val distanceInMeters : Int = 0,
    val timeInMillis : Long = 0L,
    val caloriesBurned : Int = 0
)
