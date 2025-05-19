package com.example.worldchangingcookingapp.services

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json


class ApiService(private val context: Context) {
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

    suspend fun addRecipe(self: User, recipe: Recipe): String {
        return addRecipe(self.id!!, recipe)
    }

    suspend fun addRecipe(userId: String, recipe: Recipe): String {
        val id = firestore.collection(RECIPE_COLLECTION).add(recipe).await().id
        //firestore.collection(RECIPE_COLLECTION).add(recipe).await()

        val userRef = firestore.collection(USER_COLLECTION).document(userId)
        userRef.update(RECIPE_FIELD, FieldValue.arrayUnion(id)).await()
        return id
    }

    fun launchRecipeAddWorker(user: User, recipe: Recipe) {
        val recipeString = Json.encodeToString(recipe)

        val inputData = Data.Builder()
            .putString(AddRecipeWorker.USER_ID, user.id!!)
            .putString(AddRecipeWorker.RECIPE_JSON, recipeString)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val addRecipeWorker = OneTimeWorkRequestBuilder<AddRecipeWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(addRecipeWorker)
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        val userRef = firestore.collection(USER_COLLECTION).document(recipe.authorId)
        userRef.update(RECIPE_FIELD, FieldValue.arrayRemove(recipe.id)).await()

        firestore.collection(RECIPE_COLLECTION).document(recipe.id).delete().await()
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
    suspend fun loadFeed(self: User, lastVisible: DocumentSnapshot? = null):
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

    /*
     * This returns a flow that automatically
     * provides the 10 most recent recipes.
     */
    fun getFeed(self: User): Flow<List<Recipe>> = callbackFlow {

        val query = firestore.collection(RECIPE_COLLECTION)
            .orderBy("publicationDate", Query.Direction.DESCENDING)

        val subscription = query.addSnapshotListener { snapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val recipes = snapshot.documents.mapNotNull {
                    doc -> val recipe = doc.toObject(Recipe::class.java)
                    if (recipe != null && recipe.authorId != self.id) recipe.copy(id = doc.id) else null
                }
                trySend(recipes).isSuccess
            } else {
                trySend(emptyList()).isSuccess
            }
        }

        awaitClose {
            subscription.remove()
        }

    }


    fun randomRecipeId(): String {
        return firestore.collection(RECIPE_COLLECTION).document().id
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val RECIPE_COLLECTION = "recipes"
        private const val FRIENDS_FIELD = "friends"
        private const val RECIPE_FIELD = "recipes"
    }
}