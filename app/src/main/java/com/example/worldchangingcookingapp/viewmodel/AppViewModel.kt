package com.example.worldchangingcookingapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.WorldChangingCookingApplication
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.services.DatabaseService
import kotlinx.coroutines.launch

class AppViewModel (
    val auth: AccountService,
    val api: ApiService,
    val database: DatabaseService) : ViewModel() {

    val loggedIn = mutableStateOf(auth.hasUser)
    val user = mutableStateOf<User?>(null)

    fun signIn() {
        loggedIn.value = auth.hasUser
        viewModelScope.launch {
            user.value = api.getUser(auth.currentUserId)
            print("User ${user.value?.displayName} Signed In")
        }
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut()
            loggedIn.value = auth.hasUser
            user.value = null
        }
    }

    companion object {
        val Factory =
            viewModelFactory {
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WorldChangingCookingApplication)
                    val accountService = application.container.authService
                    val apiService = application.container.apiService
                    val databaseService = application.container.databaseService
                    AppViewModel(accountService, apiService, databaseService)
                }
            }
    }
}