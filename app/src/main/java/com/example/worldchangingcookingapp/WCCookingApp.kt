package com.example.worldchangingcookingapp

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.ui.screens.CreateRecipeScreen
import com.example.worldchangingcookingapp.ui.screens.LoginScreen
import com.example.worldchangingcookingapp.ui.theme.WorldChangingCookingAppTheme
import com.example.worldchangingcookingapp.viewmodel.LoginViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext


@Composable
fun WCCookingApp() {
    WorldChangingCookingAppTheme {
        Surface (color = MaterialTheme.colorScheme.background) {
            var navController = rememberNavController()

            val accountService = remember { AccountService() }

            val initialScreen = if (accountService.hasUser) Home else Login

            Scaffold (
                    bottomBar = { if (accountService.hasUser) { BottomNavBar(navController) } }
            ){
                innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = initialScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        appGraph(navController, accountService)
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


fun NavGraphBuilder.appGraph(navController : NavController, auth : AccountService) {
    composable<Home> {
        val coroutineScope = rememberCoroutineScope()
        Button(onClick = {
            coroutineScope.launch {
                auth.signOut()
            }
        }) {
            Text("Sign Out")
        }
    }
    composable<Profile> {  }
    composable<CreateRecipe> { CreateRecipeScreen() }
    composable<ViewRecipe> { }
    composable<Login> {
        val viewModel : LoginViewModel = viewModel(
            factory = LoginViewModel.Factory(auth)
        )
        LoginScreen(viewModel, onSuccess = {
            navController.navigate(Home) {
                launchSingleTop = true
            }
        })
    }
}