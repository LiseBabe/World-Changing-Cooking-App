package com.example.worldchangingcookingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.contants.CacheCategory
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.services.DatabaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
 * Handles state for the drafts screen.
 * Requires DatabaseService for loading and deleting recipes
 * from cache.
 */
class DraftsViewModel (private val db : DatabaseService) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes : StateFlow<List<Recipe>> = _recipes

    init {
        getDrafts()
    }

    /*
     * Get all recipes from the database that are drafts.
     */
    fun getDrafts() {
        viewModelScope.launch {
            _recipes.value = db.getRecipes(CacheCategory.DRAFT)
        }
    }

    /*
     * Deletes a recipe from the cache
     */
    fun deleteDraft(id : String) {
        viewModelScope.launch {
            db.deleteRecipe(id)
            getDrafts()
        }
    }


    companion object {
        val Factory = { db : DatabaseService ->
            viewModelFactory {
                initializer {
                    DraftsViewModel(db)
                }
            }
        }
    }
}