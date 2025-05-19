package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.worldchangingcookingapp.contants.ScreenType
import com.example.worldchangingcookingapp.contants.ViewRecipe
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.viewmodel.AppViewModel
import com.example.worldchangingcookingapp.viewmodel.HomePageViewModel
import com.example.worldchangingcookingapp.viewmodel.UserState

@Composable
fun HomePageScreen(homePageViewModel: HomePageViewModel, screenType: ScreenType, onSelect: (Recipe) -> Unit, onUser: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        Text("Logged in as: ${homePageViewModel.user?.email ?: "Unknown"}")
//        Text("User ID: ${homePageViewModel.user?.id ?: "Unknown"}")


        RecipeListScreen(
            recipes = homePageViewModel.recipes.collectAsState().value,
            screenType = screenType,
            onRecipeClick = onSelect,
            onUserClick = onUser
        )
    }
}