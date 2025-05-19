package com.example.worldchangingcookingapp.services

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


/*
 * Manages the firebase authentication process
*/
class AccountService {
    private val auth: FirebaseAuth = Firebase.auth

    val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    val hasUser: Boolean
        get() = auth.currentUser != null

    /*
     * Login a user given an email and password
     * Throws an error if bad login attempt
     */
    suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    /*
     * Create an account with given email and password
     * Throws an error if bad create attempt (email already used)
     */
    suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    /*
     * Signs out the user
     */
    fun signOut() {
        auth.signOut()
    }
}