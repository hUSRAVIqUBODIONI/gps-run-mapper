package com.example.runapp.db.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runapp.db.dao.LocationDAO
import com.example.runapp.db.dao.RunDAO
import com.example.runapp.db.entities.Location
import com.example.runapp.db.entities.Run


@Database(entities = [Run::class, Location::class], version = 2,exportSchema = false)
abstract class RunDataBase : RoomDatabase(){

    abstract fun runDAO() : RunDAO

    abstract fun locationDAO(): LocationDAO

}