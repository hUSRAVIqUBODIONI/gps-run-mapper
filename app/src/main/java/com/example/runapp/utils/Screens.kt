package com.example.runapp.utils

import androidx.compose.ui.res.stringResource
import com.example.runapp.R


enum class Screens(val title : Int) {
    Run(R.string.app_name),
    CurrentRun(R.string.current_run),
    Statistic(R.string.your_statistic),
}