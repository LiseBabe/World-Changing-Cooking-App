package com.example.worldchangingcookingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.form.RecipeForm
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class PostResult {
    object Success : PostResult()
    data class Error(val message : String) : PostResult()
}

class RecipeFormViewModel (val user: User, val api: ApiService) : ViewModel() {
    var form = RecipeForm()

    private val _postStatus = MutableSharedFlow<PostResult>()
    val postStatus = _postStatus.asSharedFlow()

    fun getRecipe() : Recipe {
        val recipe = form.toRecipe()
        recipe.authorId = user.id!!
        recipe.authorName = user.displayName
        recipe.authorProfilePath = user.profilePicturePath
        return recipe
    }

    fun publishRecipe() {
        form.validate()
        if(!form.isValid)  {
            viewModelScope.launch {
                _postStatus.emit(PostResult.Error("Recipe Not Complete"))
            }
            return
        }
        val recipe = getRecipe()
        viewModelScope.launch {
            try {
                api.addRecipe(user, recipe)
                _postStatus.emit(PostResult.Success)
            } catch (e : Exception) {
                _postStatus.emit(PostResult.Error(e.message ?: "Post Failed"))
            }

        }
    }

    companion object {
        val Factory = { user : User, apiService : ApiService ->
            viewModelFactory {
                initializer {
                    RecipeFormViewModel(user, apiService)
                }
            }
        }
    }
}