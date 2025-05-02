package com.example.worldchangingcookingapp.database

import com.example.worldchangingcookingapp.models.User

class Users {
    fun getUsers(): List<User>{
        return listOf(
            User(
                0,
                "user0",
                "user 0",
                1,
                2
            ),
            User(
                1,
                "user1",
                "user 1",
                4,
                3
            ),
            User(
                1,
                "user2",
                "user 2",
                10,
                0
            )
        )
    }
}