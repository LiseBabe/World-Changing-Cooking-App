package com.example.worldchangingcookingapp.database

import android.content.Context
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.services.DatabaseService

// Interface that defines the application-level dependencies
interface AppContainer {
    val authService: AccountService        // Responsible for user authentication
    val apiService: ApiService             // Handles network/API calls
    val databaseService: DatabaseService   // Manages local database operations
}

// Concrete implementation of AppContainer that initializes the services
class DefaultAppContainer(private val context: Context) : AppContainer {

    // Lazily initialize the authentication service
    override val authService: AccountService by lazy {
        AccountService()
    }

    // Lazily initialize the API service using the provided context
    override val apiService: ApiService by lazy {
        ApiService(context)
    }

    // Lazily initialize the database service with the DAO from the Room database
    override val databaseService: DatabaseService by lazy {
        DatabaseService(RecipeDatabase.getDatabase(context).recipeDao())
    }
}
