package com.example.worldchangingcookingapp.services

import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ApiService {
    private val firestore : FirebaseFirestore = Firebase.firestore

    suspend fun getUser(id: String): User? {
        return firestore.collection(USER_COLLECTION)
            .document(id).get()
            .await().toObject(User::class.java)
    }

    suspend fun updateUser(user: User) {
        firestore.collection(USER_COLLECTION).document(user.id).set(user).await()
    }

    suspend fun getFriends(ids: List<String>): List<User>? {
        return null
    }

    suspend fun addFriend(self: User, id: String) {}

    suspend fun removeFriend(self: User, id: String) {}

    suspend fun deleteAccount(self: User) {}

    suspend fun getRecipe(id: String): Recipe? {
        return null
    }

    suspend fun addRecipe(recipe: Recipe) {}

    suspend fun deleteRecipe(id: String) {}

    suspend fun getRecipes(ids: List<String>): List<Recipe>? {
        return null
    }

    suspend fun getFeed(self: User): List<Recipe>? {
        return null
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val RECIPE_COLLECT = "recipes"
    }
}