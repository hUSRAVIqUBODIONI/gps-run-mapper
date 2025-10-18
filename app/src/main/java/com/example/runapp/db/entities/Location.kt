package com.example.runapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runapp.data.location.LocationData


@Entity(tableName = "locations")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lat: Double,
    val lng: Double,
    val timestamp: Long,
)


fun Location.toLocationData() : LocationData{
    return LocationData(
        latitude = this.lat,
        longitude = this.lng,
        timestamp = this.timestamp
    )
}
