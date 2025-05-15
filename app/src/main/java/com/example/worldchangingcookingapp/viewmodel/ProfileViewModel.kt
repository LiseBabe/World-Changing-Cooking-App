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
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.services.DatabaseService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProfileViewModel(
    user : User?,
    apiService : ApiService,
    databaseService: DatabaseService
) : ViewModel() {
    var user by mutableStateOf(user)
    var api by mutableStateOf(apiService)
    var recipes by mutableStateOf<List<Recipe>?>(emptyList())
    var isRecipesLoading by mutableStateOf(false)

    fun loadUserRecipes() {
        viewModelScope.launch {
            isRecipesLoading = true
            recipes = api.getRecipes(user?.recipes!!)
            isRecipesLoading = false
        }
    }

    companion object {
        val Factory = { user : User?, apiService : ApiService, databaseService: DatabaseService ->
            viewModelFactory {
                initializer {
                    ProfileViewModel(user, apiService, databaseService)
                }
            }
        }
    }
}