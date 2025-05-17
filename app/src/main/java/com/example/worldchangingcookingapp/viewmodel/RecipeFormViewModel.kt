package com.example.worldchangingcookingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.contants.CacheCategory
import com.example.worldchangingcookingapp.form.RecipeForm
import com.example.worldchangingcookingapp.models.CookingType
import com.example.worldchangingcookingapp.models.Difficulty
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.TypeOfRecipe
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.services.DatabaseService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

sealed class PostResult {
    object Success : PostResult()
    data class Error(val message : String) : PostResult()
}

class RecipeFormViewModel (
    userState: UserState,
    val api: ApiService,
    val db: DatabaseService,
    var recipe : Recipe?
) : ViewModel() {
    var form = RecipeForm()

    val user = when (userState) {
        is UserState.SignedIn -> {
            userState.user
        }
        else -> null
    }

    private val _postStatus = MutableSharedFlow<PostResult>()
    val postStatus = _postStatus.asSharedFlow()

    init {
        recipe?.let { form.setupForm(it) }
    }

    fun buildRecipe() : Recipe {
        if (recipe == null) {
            recipe = Recipe()
        }
        form.collectValues(recipe!!)
        recipe?.authorId = user!!.id!!
        recipe?.authorName = user!!.displayName
        recipe?.authorProfilePath = user!!.profilePicturePath
        return recipe!!
    }

    fun publishRecipe() {
        form.validate()
        if(!form.isValid)  {
            viewModelScope.launch {
                _postStatus.emit(PostResult.Error("Recipe Not Complete"))
            }
            return
        }
        recipe = buildRecipe()
        viewModelScope.launch {
            try {
                if (recipe?.id != null && recipe?.id != "") {
                    deleteFromCache(recipe?.id!!)
                }
                val id = api.addRecipe(user!!, recipe!!)
                _postStatus.emit(PostResult.Success)
                recipe?.cacheCategory = CacheCategory.REGULAR
                recipe?.id = id
                saveRecipe(recipe!!)
            } catch (e : Exception) {
                _postStatus.emit(PostResult.Error(e.message ?: "Post Failed"))
            }

        }
    }

    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            db.insertRecipe(recipe)
        }
    }

    fun deleteFromCache(id: String) {
        viewModelScope.launch {
            db.deleteRecipe(id)
        }
    }

    companion object {
        val Factory = { user : UserState, apiService : ApiService, databaseService: DatabaseService, recipe : Recipe? ->
            viewModelFactory {
                initializer {
                    RecipeFormViewModel(user, apiService, databaseService, recipe)
                }
            }
        }
    }
}