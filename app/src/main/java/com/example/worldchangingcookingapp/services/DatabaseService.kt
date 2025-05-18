package com.example.worldchangingcookingapp.services

import com.example.worldchangingcookingapp.contants.CacheCategory
import com.example.worldchangingcookingapp.database.RecipeDataAccessObject
import com.example.worldchangingcookingapp.models.Recipe

class DatabaseService(private val recipeDao: RecipeDataAccessObject) {
    suspend fun getRecipes(cacheType: CacheCategory): List<Recipe> {
        return recipeDao.getRecipes(cacheType.name)
    }

    suspend fun getRecipe(id: String): Recipe {
        return recipeDao.getRecipe(id)
    }

    suspend fun deleteRecipe(id: String) {
        recipeDao.deleteRecipe(id)
    }

    suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }
}