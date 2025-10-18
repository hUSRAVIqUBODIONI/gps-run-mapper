package com.example.runapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.runapp.data.location.LocationData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

class DefaultLocationTracker(private val context: Context) : LocationTracker {

    private val client = LocationServices.getFusedLocationProviderClient(
        context
    )

    @SuppressLint("MissingPermission")
    override fun getLocation(): Flow<LocationData>  = callbackFlow{
       val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,3000).build()
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    trySend(location.toLocation())
                }
            }
        }
        client.requestLocationUpdates(request, callback, Looper.getMainLooper())


        awaitClose{
            client.removeLocationUpdates(callback)
        }
    }


    private fun Location.toLocation() : LocationData {
        return LocationData(
            latitude = this.latitude,
            longitude = this.longitude,
            timestamp = this.time
        )

    }

}