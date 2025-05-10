package com.example.worldchangingcookingapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.worldchangingcookingapp.contants.CreateRecipe
import com.example.worldchangingcookingapp.contants.Drafts
import com.example.worldchangingcookingapp.contants.Home
import com.example.worldchangingcookingapp.contants.Login
import com.example.worldchangingcookingapp.contants.Profile
import com.example.worldchangingcookingapp.contants.ViewRecipe
import com.example.worldchangingcookingapp.contants.topLevelRoutes
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.ui.screens.CreateRecipeScreen
import com.example.worldchangingcookingapp.ui.screens.DraftsScreen
import com.example.worldchangingcookingapp.ui.screens.LoginScreen
import com.example.worldchangingcookingapp.ui.theme.WorldChangingCookingAppTheme
import com.example.worldchangingcookingapp.viewmodel.AppViewModel
import com.example.worldchangingcookingapp.viewmodel.DraftsViewModel
import com.example.worldchangingcookingapp.viewmodel.LoginViewModel
import com.example.worldchangingcookingapp.viewmodel.RecipeFormViewModel


@Composable
fun WCCookingApp() {
    val appViewModel : AppViewModel = viewModel(
        factory = AppViewModel.Factory
    )
    appViewModel.signIn()

    val loggedIn by remember { appViewModel.loggedIn }

    WorldChangingCookingAppTheme {

        Surface (color = MaterialTheme.colorScheme.background) {
            var navController = rememberNavController()

            val initialScreen = if (loggedIn) Home else Login

            Scaffold (
                    bottomBar = { if (loggedIn) { BottomNavBar(navController) } }
            ){
                innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = initialScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        appGraph(navController, appViewModel)
                    }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.label) },
                label = { Text(topLevelRoute.label) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


fun NavGraphBuilder.appGraph(navController : NavController, appViewModel : AppViewModel) {
    composable<Home> {
        val user by remember { appViewModel.user }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Logged in As: ${user?.email}")
            Text("Logged in Id: ${user?.id}")

            Button(onClick = {
                appViewModel.signOut()
            }) {
                Text("Sign Out")
            }
        }
    }
    composable<Profile> { }
    composable<Drafts> {
        val viewModel: DraftsViewModel = viewModel(
            factory = DraftsViewModel.Factory(appViewModel.database)
        )
        DraftsScreen(viewModel) { recipe ->
            appViewModel.selectedRecipe = recipe
            navController.navigate(CreateRecipe) {
                launchSingleTop = true
            }
        }
    }
    composable<CreateRecipe> {
        val viewModel: RecipeFormViewModel = viewModel(
            factory = RecipeFormViewModel.Factory(
                appViewModel.user.value!!,
                appViewModel.api,
                appViewModel.database,
                appViewModel.selectedRecipe
            )
        )
        CreateRecipeScreen(viewModel) {
            navController.navigate(Drafts)
        }
    }
    composable<ViewRecipe> { }
    composable<Login> {
        val viewModel: LoginViewModel = viewModel(
            factory = LoginViewModel.Factory(appViewModel.auth, appViewModel.api)
        )
        LoginScreen(viewModel, onSuccess = {
            appViewModel.signIn()
            navController.navigate(Home) {
                launchSingleTop = true
            }
        })
    }
}