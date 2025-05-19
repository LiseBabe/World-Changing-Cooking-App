package com.example.worldchangingcookingapp.services

import com.example.worldchangingcookingapp.contants.CacheCategory
import com.example.worldchangingcookingapp.database.RecipeDataAccessObject
import com.example.worldchangingcookingapp.models.Recipe


/*
 * Container for all the room database functionality.
 */
class DatabaseService(private val recipeDao: RecipeDataAccessObject) {
    /*
     * Returns recipes from the database filtered by cacheType.
     * Currently only used to retrieve cached drafted recipes.
     */
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