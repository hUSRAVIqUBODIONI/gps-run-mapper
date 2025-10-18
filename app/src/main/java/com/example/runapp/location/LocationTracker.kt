package com.example.runapp.location

import com.example.runapp.data.location.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationTracker  {
    fun getLocation() : Flow<LocationData>
}