package com.example.worldchangingcookingapp.contants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Profile
@Serializable
object EditProfile
@Serializable
object CreateRecipe
@Serializable
object ViewRecipe
@Serializable
object Login
@Serializable
object Drafts

data class TopLevelRoute<T : Any>(val route : T, val icon : ImageVector, val label: String)

val topLevelRoutes = listOf(
    TopLevelRoute(Home, Icons.Default.Home, "Home"),
    TopLevelRoute(Profile, Icons.Default.Person, "Profile"),
    TopLevelRoute(Drafts, Icons.Default.Create, "Create Recipes")
)