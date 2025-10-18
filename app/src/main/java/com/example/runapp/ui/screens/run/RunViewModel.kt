package com.example.runapp.ui.screens.run

import androidx.lifecycle.ViewModel
import com.example.runapp.db.repository.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RunViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel (){

    val runs = runRepository.allRunByAvgSpeedInKMH
}