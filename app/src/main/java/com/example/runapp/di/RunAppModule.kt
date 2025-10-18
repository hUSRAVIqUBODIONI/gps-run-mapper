package com.example.runapp.di

import android.content.Context
import androidx.room.Room
import com.example.runapp.db.DataBase.RunDataBase
import com.example.runapp.db.dao.LocationDAO
import com.example.runapp.db.dao.RunDAO
import com.example.runapp.location.DefaultLocationTracker
import com.example.runapp.location.LocationTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class RunAppModule {

    @Provides
    fun provideDataBase(@ApplicationContext context: Context) : RunDataBase {
        return Room.databaseBuilder(
            context = context,
            RunDataBase::class.java,
            "run_db"
        ).build()

    }

    @Provides
    fun provideRunDao(dataBase: RunDataBase) : RunDAO{
        return dataBase.runDAO()
    }

    @Provides
    fun provideLocationDao(dataBase: RunDataBase) : LocationDAO{
        return dataBase.locationDAO()
    }

    @Provides
    fun provideDefaultLocationTracker(@ApplicationContext context: Context) : LocationTracker{
        return DefaultLocationTracker(context)
    }
}