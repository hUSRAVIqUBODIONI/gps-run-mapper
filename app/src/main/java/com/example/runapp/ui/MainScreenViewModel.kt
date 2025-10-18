package com.example.runapp.ui

import androidx.lifecycle.ViewModel
import com.example.runapp.db.repository.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val mainRepository: RunRepository
)  : ViewModel() {


}