package com.example.runapp

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
        Timber.plant(Timber.DebugTree())
    }
}