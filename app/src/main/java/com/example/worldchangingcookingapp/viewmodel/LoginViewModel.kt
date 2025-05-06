package com.example.worldchangingcookingapp.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginViewModel (private val auth : AccountService, private val api : ApiService) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordDupe by mutableStateOf("")

    var creatingAccount by mutableStateOf(false)
    var reload by mutableStateOf(false)
    var hasError by mutableStateOf(false)

    fun login() {
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            hasError = true
            return
        }
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                hasError = true
                print("Error Logging In: ${throwable.message!!}")
            }
        ){
            auth.authenticate(username, password)
            hasError = false
            reload = true
        }
    }

    fun createAccount() {
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            hasError = true
            return
        }
        if (password != passwordDupe || password.length < 6) {
            hasError = true
            return
        }
        viewModelScope.launch (
            CoroutineExceptionHandler { _, throwable ->
                hasError = true
                print("Error Creating Account: ${throwable.message!!}")
            }
        ){
            auth.createAccount(username, password)
            api.updateUser(User(id = auth.currentUserId, email = username))
            hasError = false
            reload = true
        }
    }

    companion object {
        val Factory = { accountService : AccountService, apiService : ApiService ->
            viewModelFactory {
                initializer {
                    LoginViewModel(accountService, apiService)
                }
            }
        }
    }
}