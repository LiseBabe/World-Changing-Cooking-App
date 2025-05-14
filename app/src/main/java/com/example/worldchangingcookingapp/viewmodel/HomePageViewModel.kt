package com.example.worldchangingcookingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.data.FakeRecipeDatabase
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.services.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(
    private val api: ApiService
) : ViewModel() {

    // recipe list
    private val _recipes = MutableStateFlow<List<Recipe>>(FakeRecipeDatabase.recipes)
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    companion object {
        val Factory = { api: ApiService ->
            viewModelFactory {
                initializer {
                    HomePageViewModel(api)
                }
            }
        }
    }
}
