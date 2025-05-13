package com.example.worldchangingcookingapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import kotlinx.coroutines.launch

class AppViewModel (val auth: AccountService, val api: ApiService) : ViewModel() {

    val loggedIn = mutableStateOf(auth.hasUser)
    val user = mutableStateOf<User?>(null)

    fun signIn() {
        loggedIn.value = auth.hasUser
        viewModelScope.launch {
            user.value = api.getUser(auth.currentUserId)
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
        val Factory = { accountService: AccountService,
            apiService: ApiService ->
            viewModelFactory {
                initializer {
                    AppViewModel(accountService, apiService)
                }
            }
        }
    }
}