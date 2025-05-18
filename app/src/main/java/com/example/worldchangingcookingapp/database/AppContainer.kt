package com.example.worldchangingcookingapp.database

import android.content.Context
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.services.DatabaseService

interface AppContainer {
    val authService : AccountService
    val apiService : ApiService
    val databaseService : DatabaseService
}

class DefaultAppContainer(private val context : Context) : AppContainer {
    override val authService: AccountService by lazy {
        AccountService()
    }

    override val apiService: ApiService by lazy {
        ApiService(context)
    }

    override val databaseService: DatabaseService by lazy {
        DatabaseService(RecipeDatabase.getDatabase(context).recipeDao())
    }
}