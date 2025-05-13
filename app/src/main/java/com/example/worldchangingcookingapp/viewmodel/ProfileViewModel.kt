package com.example.worldchangingcookingapp.viewmodel

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import kotlinx.coroutines.launch

class ProfileViewModel(private val auth : AccountService, private val api : ApiService) : ViewModel() {
    val user = mutableStateOf<User?>(null)

    var reload by mutableStateOf(false)
    var hasError by mutableStateOf(false)


    fun init() {
        viewModelScope.launch {
            user.value = api.getUser(auth.currentUserId)
        }
    }

    fun changeUserUsername(){
        viewModelScope.launch {
            //api.updateUser()
        }
    }

    fun changeUserProfilePicture(){

    }

    companion object {
        val Factory = { accountService : AccountService, apiService : ApiService ->
            viewModelFactory {
                initializer {
                    ProfileViewModel(accountService, apiService)
                }
            }
        }
    }
}