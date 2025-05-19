package com.example.worldchangingcookingapp.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.worldchangingcookingapp.models.Recipe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json


/*
 * Worker responsible for publishing new recipes.
 * Will wait to publish until internet connection is established.
 * Returns the publish recipe's id as outputData.
 */
class AddRecipeWorker (ctx: Context, params: WorkerParameters, private val api: ApiService):
 CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val userId = inputData.getString(USER_ID)
        val recipeJson = inputData.getString(RECIPE_JSON)

        if (userId.isNullOrEmpty() || recipeJson.isNullOrEmpty()) {
            return Result.failure()
        }

        val recipe : Recipe = try {
            Json.decodeFromString<Recipe>(recipeJson)
        } catch (e: SerializationException) {
            return Result.failure()
        }

        return try {
            val id = api.addRecipe(userId, recipe)
            val outputData = Data.Builder()
                .putString(RECIPE_ID, id)
                .build()
            Result.success(outputData)
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val USER_ID = "USER_ID"
        const val RECIPE_JSON = "RECIPE_JSON"
        const val RECIPE_ID = "RECIPE_ID"
        class AddRecipeWorkerFactory(private val api: ApiService) : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return when (workerClassName) {
                    AddRecipeWorker::class.java.name -> AddRecipeWorker(
                        appContext,
                        workerParameters,
                        api
                    )
                    else -> null
                }
            }
        }
    }
}