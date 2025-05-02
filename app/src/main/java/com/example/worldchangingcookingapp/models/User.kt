package com.example.worldchangingcookingapp.models

data class User(
    var id: Long = 0L,
    var pseudo: String,
    var name: String,
    var numberRecipes: Long,
    var numberFriends: Long
)
