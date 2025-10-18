package com.example.runapp.location

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.runapp.R
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber



@AndroidEntryPoint
class LocationService : Service() {


    @Inject
    lateinit var locationTracker: LocationTracker
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var locationJob: Job? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag("MyLocation").d("Received intent action: ${intent?.action}")
        when (intent?.action) {
            ACTION_START -> {
                startForegroundService()
                startLocationUpdates()
            }

            ACTION_STOP -> {
                stopForeground(true)
                stopSelf()
            }
        }
        return START_STICKY
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        try {
            val notification = NotificationCompat.Builder(this, "tracking_service_channel")
                .setContentTitle("Service Running")
                .setContentText("Tracking location")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)  // Системная иконка
                .build()

            startForeground(1, notification)

        } catch (e: Exception) {
            Timber.tag("MyLocation").e(e, "Failed to start foreground service")
        }
    }


    private fun startLocationUpdates() {
        locationJob = serviceScope.launch {
            locationTracker.getLocation().collect { location ->
                val broadcastIntent = Intent("LOCATION_UPDATE").apply {
                    putExtra("lat", location.latitude)
                    putExtra("lon", location.longitude)
                    putExtra("tmp",location.timestamp)
                    Timber.tag("MyLocation").d("here")
                }

                sendBroadcast(broadcastIntent)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationJob?.cancel()
    }



    companion object {
        const val ACTION_START = "start_action"
        const val ACTION_STOP = "stop_action"
    }
}