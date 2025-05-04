package com.example.worldchangingcookingapp.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.services.AccountService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginViewModel (private val auth : AccountService) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordDupe by mutableStateOf("")

    var creatingAccount by mutableStateOf(false)
    var reload by mutableStateOf(false)

    fun login() {
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) return
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                print("Error Logging In: ${throwable.message!!}")
            }
        ){
            auth.authenticate(username, password)
            reload = true
        }
    }

    fun createAccount() {
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) return
        if (password != passwordDupe || password.length < 6) return
        viewModelScope.launch (
            CoroutineExceptionHandler { _, throwable ->
                print("Error Creating Account: ${throwable.message!!}")
            }
        ){
            auth.createAccount(username, password)
            reload = true
        }
    }

    companion object {
        val Factory = { accountService : AccountService ->
            viewModelFactory {
                initializer {
                    LoginViewModel(accountService)
                }
            }
        }
    }
}