package com.example.worldchangingcookingapp.models

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val email: String = "",
    val displayName: String = "",
)
