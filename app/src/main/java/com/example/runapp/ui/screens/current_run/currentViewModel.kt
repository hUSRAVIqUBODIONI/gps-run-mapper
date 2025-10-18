package com.example.runapp.ui.screens.current_run

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.runapp.data.location.LocationData
import com.example.runapp.db.entities.Location
import com.example.runapp.db.entities.toLocationData
import com.example.runapp.db.repository.RunRepository
import com.example.runapp.location.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CurrentRunViewModel @Inject constructor(
    private val repository: RunRepository,
) : ViewModel() {

    val path: StateFlow<List<LocationData>> = repository.path.map { locations ->
        locations.map { location ->
            location.toLocationData()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    var parsedTime = MutableStateFlow(0L)
    var timerStarted = MutableStateFlow(false)


    fun addLocation(lat: Double, lon: Double, tmp: Long) {

        viewModelScope.launch {
            repository.insertLocation(
                Location(
                    lat = lat,
                    lng = lon,
                    timestamp = tmp
                )
            )
        }


    }

    fun startTimer() {
        timerStarted.value = true

    }

    fun stopTimer() {
        viewModelScope.launch {
            repository.deleteAllLocations()
        }
        timerStarted.value = false
        parsedTime.value = 0L
    }



}
