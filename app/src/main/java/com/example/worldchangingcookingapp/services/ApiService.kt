package com.example.worldchangingcookingapp.services

import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ApiService {
    private val firestore = Firebase.firestore

    suspend fun getUser(id: String): User? {
        return firestore.collection(USER_COLLECTION)
            .document(id).get()
            .await().toObject(User::class.java)
    }

    suspend fun updateUser(user: User) {
        firestore.collection(USER_COLLECTION).document(user.id!!).set(user).await()
    }

    suspend fun getFriends(ids: List<String>): List<User>? {
        val friends = mutableListOf<User>()

        for (chunk in ids.chunked(10)) {
            val snapshot = firestore.collection(USER_COLLECTION)
                .whereIn(FieldPath.documentId(), chunk)
                .get()
                .await()

            val results = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
            friends.addAll(results)
        }
        return friends
    }

    /*
    * Forcibly adds a friend, requests not yet implemented
    * Only makes you friends with them not them friends with you
    * might change later
    *  */
    suspend fun addFriend(self: User, id: String) {
        val userRef = firestore.collection(USER_COLLECTION).document(self.id!!)
        userRef.update(FRIENDS_FIELD, FieldValue.arrayUnion(id)).await()
    }

    /*
    * Forcibly removes a friend
    * Only removes them from users friend list
    * might change later
    *  */
    suspend fun removeFriend(self: User, id: String) {
        val userRef = firestore.collection(USER_COLLECTION).document(self.id!!)
        userRef.update(FRIENDS_FIELD, FieldValue.arrayRemove(id)).await()
    }

    suspend fun deleteAccount(self: User) {

    }

    suspend fun getRecipe(id: String): Recipe? {
        return firestore.collection(RECIPE_COLLECTION).document(id).get().await()
            .toObject(Recipe::class.java)
    }

    suspend fun addRecipe(self: User, recipe: Recipe) {
        firestore.collection(RECIPE_COLLECTION).add(recipe).await()

        val userRef = firestore.collection(USER_COLLECTION).document(self.id!!)
        userRef.update(RECIPE_FIELD, FieldValue.arrayUnion(recipe.id)).await()
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        val userRef = firestore.collection(USER_COLLECTION).document(recipe.authorId)
        userRef.update(RECIPE_FIELD, FieldValue.arrayRemove(recipe.id)).await()

        firestore.collection(RECIPE_COLLECTION).document(recipe.id!!).delete().await()
    }

    suspend fun getRecipes(ids: List<String>): List<Recipe>? {
        val recipes = mutableListOf<Recipe>()

        for (chunk in ids.chunked(10)) {
            val snapshot = firestore.collection(RECIPE_COLLECTION)
                .whereIn(FieldPath.documentId(), chunk)
                .get()
                .await()

            val results = snapshot.documents.mapNotNull { it.toObject(Recipe::class.java) }
            recipes.addAll(results)
        }
        return recipes
    }


    /*
     * For now this returns the most recent publications.
     *
     * Will return 10 on the first call and then 10 more with each
     * subsequent call (pass back in the returned lastVisible)
     *
     * Want to add friend only support but that is impractical atm.
     */
    suspend fun getFeed(self: User, lastVisible: DocumentSnapshot? = null):
            Pair<List<Recipe>, DocumentSnapshot?> {
        var query = firestore.collection(RECIPE_COLLECTION)
            .orderBy("publicationDate", Query.Direction.DESCENDING)
            .limit(10)

        if (lastVisible != null) {
            query = query.startAfter(lastVisible)
        }

        val snapshots = query.get().await()

        val recipes = snapshots.documents.mapNotNull {
            doc ->
            val recipe = doc.toObject(Recipe::class.java)
            if (recipe != null && recipe.authorId != self.id) recipe.copy(id = doc.id) else null
        }

        val newLastVisible = if (snapshots.documents.isNotEmpty()) {
            snapshots.documents.last()
        } else {
            null
        }
        return recipes to newLastVisible
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val RECIPE_COLLECTION = "recipes"
        private const val FRIENDS_FIELD = "friends"
        private const val RECIPE_FIELD = "recipes"
    }
}