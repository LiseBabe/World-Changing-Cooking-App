package com.example.worldchangingcookingapp.models

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String? = null,
    val email: String = "",
    val displayName: String = "",
    val profilePicturePath: String = "",
    val friends: List<String> = listOf(),
    val recipes: List<String> = listOf()
)
