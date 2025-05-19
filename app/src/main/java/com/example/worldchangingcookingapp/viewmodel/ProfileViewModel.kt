package com.example.worldchangingcookingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.services.DatabaseService
import kotlinx.coroutines.launch

/*
 * ViewModel for the Profile screen.
 * Manages user profile data, recipes, and friends.
 * Responsible for loading and exposing this data to the UI layer.
 */
class ProfileViewModel(
    userState: UserState,                   // Represents the current authentication state
    apiService: ApiService,                 // Service to fetch data from remote API
    databaseService: DatabaseService        // Placeholder for potential local storage operations
) : ViewModel() {

    // Store the currently signed-in user or null if not signed in
    var user = when (userState) {
        is UserState.SignedIn -> {
            userState.user
        }
        else -> null
    }

    // Hold reference to the API service (mutable to allow dynamic updates if needed)
    var api by mutableStateOf(apiService)

    // Flag to toggle between showing recipe list or friend list
    var showListRecipe by mutableStateOf(true)

    // Holds list of user's recipes
    var recipes by mutableStateOf<List<Recipe>?>(emptyList())

    // Loading indicator for recipes
    var isRecipesLoading by mutableStateOf(false)

    // Holds list of user's friends
    var friends by mutableStateOf<List<User>?>(emptyList())

    // Loading indicator for friends
    var isFriendsLoading by mutableStateOf(false)

    // Called on initialization to fetch both user recipes and friends
    init {
        loadUserRecipes()
        loadUserFriends()
    }

    /*
     * Loads the list of recipes associated with the user.
     * Updates loading state while fetching from API.
     */
    fun loadUserRecipes() {
        viewModelScope.launch {
            isRecipesLoading = true
            recipes = api.getRecipes(user?.recipes!!) // Assumes user and recipes are non-null
            isRecipesLoading = false
        }
    }

    /*
     * Loads the list of friends associated with the user.
     * Updates loading state while fetching from API.
     */
    fun loadUserFriends() {
        viewModelScope.launch {
            isFriendsLoading = true
            friends = api.getFriends(user?.friends!!) // Assumes user and friends are non-null
            isFriendsLoading = false
        }
    }

    /*
     * Companion object to help create the ViewModel using a factory pattern.
     * Ensures dependencies are passed correctly.
     */
    companion object {
        val Factory = { user: UserState, apiService: ApiService, databaseService: DatabaseService ->
            viewModelFactory {
                initializer {
                    ProfileViewModel(user, apiService, databaseService)
                }
            }
        }
    }
}
