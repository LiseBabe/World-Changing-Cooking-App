package com.example.worldchangingcookingapp

import android.app.Application
import com.example.worldchangingcookingapp.database.AppContainer
import com.example.worldchangingcookingapp.database.DefaultAppContainer

class WorldChangingCookingApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}