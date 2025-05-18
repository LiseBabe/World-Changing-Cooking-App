package com.example.worldchangingcookingapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.worldchangingcookingapp.models.Recipe

@Dao
interface RecipeDataAccessObject {
    @Query("SELECT * FROM recipes WHERE cacheCategory = :cacheType")
    suspend fun getRecipes(cacheType: String): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipe(id: String): Recipe

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun deleteRecipe(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)
}