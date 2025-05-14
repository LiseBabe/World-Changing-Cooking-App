package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.worldchangingcookingapp.contants.ViewRecipe
import com.example.worldchangingcookingapp.viewmodel.AppViewModel
import com.example.worldchangingcookingapp.viewmodel.HomePageViewModel

@Composable
fun HomePageScreen(navController: NavController, appViewModel: AppViewModel, homePageViewModel: HomePageViewModel) {
    val user by appViewModel.user
    val recipes = homePageViewModel.recipes.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text("Logged in as: ${user?.email ?: "Unknown"}")
        Text("User ID: ${user?.id ?: "Unknown"}")

        Spacer(modifier = Modifier.height(16.dp))

        RecipeListScreen(
            recipes = recipes,
            onRecipeClick = {
                appViewModel.selectedRecipe = it
                navController.navigate(ViewRecipe)
            }
        )


    }
}