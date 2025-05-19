package com.example.worldchangingcookingapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.worldchangingcookingapp.database.AppContainer
import com.example.worldchangingcookingapp.database.DefaultAppContainer
import com.example.worldchangingcookingapp.services.AddRecipeWorker.Companion.AddRecipeWorkerFactory
import com.google.firebase.FirebaseApp

class WorldChangingCookingApplication : Application(), Configuration.Provider {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        //Initialize firebase application
        FirebaseApp.initializeApp(this)
        //Initialize all services
        container = DefaultAppContainer(this)
        //Overwrite the default worker initialization.
        WorkManager.initialize(this, workManagerConfiguration)
    }

    /*
     * Set the worker factory to a custom factory to allow the AddRecipeWorker
     * to get the ApiService inorder to publish recipes.
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(AddRecipeWorkerFactory(container.apiService))
            .build()
}