package com.example.worldchangingcookingapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.worldchangingcookingapp.ui.screens.CreateRecipeScreen
import com.example.worldchangingcookingapp.ui.theme.WorldChangingCookingAppTheme
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Profile
@Serializable
object CreateRecipe
@Serializable
object ViewRecipe

data class TopLevelRoute<T : Any>(val route : T, val icon : ImageVector, val label: String)

val topLevelRoutes = listOf(
    TopLevelRoute(Home, Icons.Default.Home, "Home"),
    TopLevelRoute(Profile, Icons.Default.Person, "Profile"),
    TopLevelRoute(CreateRecipe, Icons.Default.Add, "Create Recipe")
)

@Composable
fun WCCookingApp() {
    WorldChangingCookingAppTheme {
        Surface (color = MaterialTheme.colorScheme.background) {
            var navController = rememberNavController()

            Scaffold (
                bottomBar = { BottomNavBar(navController) }
            ){
                innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = CreateRecipe,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        appGraph()
                    }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    BottomNavigation {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        topLevelRoutes.forEach { topLevelRoute ->
            BottomNavigationItem(
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


fun NavGraphBuilder.appGraph() {
    composable<Home> {  }
    composable<Profile> {  }
    composable<CreateRecipe> { CreateRecipeScreen() }
    composable<ViewRecipe> { }
}