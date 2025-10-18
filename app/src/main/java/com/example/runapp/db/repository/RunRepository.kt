package com.example.runapp.db.repository

import com.example.runapp.db.dao.LocationDAO
import com.example.runapp.db.dao.RunDAO
import com.example.runapp.db.entities.Location
import com.example.runapp.db.entities.Run
import javax.inject.Inject

class RunRepository @Inject constructor(
    private val runDAO: RunDAO,
    private val locationDAO: LocationDAO
){
    val allRunByCalories = runDAO.getRunByCalories()
    val allRunByTime = runDAO.getRunByTime()
    val allRunByDate  = runDAO.getRunByDate()
    val allRunByDistance = runDAO.getRunByDistance()
    val allRunByAvgSpeedInKMH = runDAO.getRunByAvgSpeedInKMH()

    val path = locationDAO.getAllLocations()


    suspend fun insertRun(run: Run){
        runDAO.insertRun(run)
    }

    suspend fun deleteRun(run: Run){
        runDAO.deleteRun(run)
    }

    suspend fun insertLocation(location: Location){
        locationDAO.insertLocation(location)
    }

    suspend fun deleteAllLocations(){
        locationDAO.deleteAllLocations()
    }

}