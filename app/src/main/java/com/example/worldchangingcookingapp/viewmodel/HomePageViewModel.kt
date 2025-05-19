package com.example.worldchangingcookingapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.services.ApiService
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.worldchangingcookingapp.models.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn


class HomePageViewModel(
    userState: UserState,
    private val api: ApiService
) : ViewModel() {

    val user = when (userState) {
        is UserState.SignedIn -> {
            userState.user
        }
        else -> null
    }

    init {
//        loadFeed(user!!)
    }

    // recipe list
    //private val _recipes = MutableStateFlow<List<Recipe>>(FakeRecipeDatabase.recipes)
    //private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    //val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()
    val recipes: StateFlow<List<Recipe>> = api.getFeed(user!!)
        .catch { error ->
            Log.e("HomePageViewModel", "Flow Error: $error.message", error)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var lastVisible: DocumentSnapshot? = null

    //debug
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()



    fun loadFeed(user: User) {
        viewModelScope.launch {
            try {
//                val (fetchedRecipes, lastDoc) = api.getFeed(user, lastVisible)
//                _recipes.value = fetchedRecipes
//                lastVisible = lastDoc
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error when request the recipe"
            }
        }
    }



    companion object {
        val Factory = { user: UserState, api: ApiService ->
            viewModelFactory {
                initializer {
                    HomePageViewModel(user, api)
                }
            }
        }
    }
}
