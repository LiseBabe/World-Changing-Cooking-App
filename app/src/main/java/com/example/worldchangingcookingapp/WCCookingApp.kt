package com.example.worldchangingcookingapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
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
import com.example.worldchangingcookingapp.contants.EditProfile
import com.example.worldchangingcookingapp.contants.Home
import com.example.worldchangingcookingapp.contants.Login
import com.example.worldchangingcookingapp.contants.Profile
import com.example.worldchangingcookingapp.contants.ViewRecipe
import com.example.worldchangingcookingapp.contants.topLevelRoutes
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.ui.screens.CreateRecipeScreen
import com.example.worldchangingcookingapp.ui.screens.DraftsScreen
import com.example.worldchangingcookingapp.ui.screens.EditProfileScreen
import com.example.worldchangingcookingapp.ui.screens.LoginScreen
import com.example.worldchangingcookingapp.ui.screens.ProfileScreen
import com.example.worldchangingcookingapp.ui.screens.HomePageScreen
import com.example.worldchangingcookingapp.ui.screens.RecipeDetailScreen
import com.example.worldchangingcookingapp.ui.screens.ViewRecipeScreen
import com.example.worldchangingcookingapp.ui.theme.WorldChangingCookingAppTheme
import com.example.worldchangingcookingapp.viewmodel.AppViewModel
import com.example.worldchangingcookingapp.viewmodel.DraftsViewModel
import com.example.worldchangingcookingapp.viewmodel.HomePageViewModel
import com.example.worldchangingcookingapp.viewmodel.LoginViewModel
import com.example.worldchangingcookingapp.viewmodel.ProfileViewModel
import com.example.worldchangingcookingapp.viewmodel.RecipeFormViewModel
import com.example.worldchangingcookingapp.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch
import com.example.worldchangingcookingapp.viewmodel.UserState


@Composable
fun WCCookingApp() {
    val appViewModel : AppViewModel = viewModel(
        factory = AppViewModel.Factory
    )

    appViewModel.signIn()

    WorldChangingCookingAppTheme {

        Surface (color = MaterialTheme.colorScheme.background) {
            var navController = rememberNavController()

            Scaffold (
                    topBar = { if (appViewModel.loggedIn) TopBar(onSignOut = { appViewModel.signOut() }) },
                    bottomBar = { if (appViewModel.loggedIn) { BottomNavBar(navController) } }
            ){
                innerPadding ->
                when(appViewModel.user) {
                    is UserState.SignedIn ->
                        NavHost(
                            navController = navController,
                            startDestination = Home,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            appGraph(navController, appViewModel)
                        }
                    is UserState.Loading -> {
                        Box (modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Refresh, "Loading")
                        }
                    }
                    else -> {
                        val viewModel: LoginViewModel = viewModel(
                            factory = LoginViewModel.Factory(appViewModel.auth, appViewModel.api)
                        )
                        LoginScreen(viewModel, onSuccess = {
                            appViewModel.signIn()
                        })
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onSignOut: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "World Changing Cooking App",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.LightGray,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.DarkGray
        ),
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Sign out") },
                    onClick = {
                        expanded = false
                        onSignOut()
                    }
                )
            }
        }
    )
}


fun NavGraphBuilder.appGraph(navController : NavController, appViewModel : AppViewModel) {
    composable<Home> {
        val homePageViewModel: HomePageViewModel = viewModel(
            factory = HomePageViewModel.Factory(appViewModel.api)
        )

        HomePageScreen(
            navController = navController,
            appViewModel = appViewModel,
            homePageViewModel = homePageViewModel,
        )
    }
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
                appViewModel.user,
                appViewModel.api,
                appViewModel.database,
                appViewModel.selectedRecipe
            )
        )
        CreateRecipeScreen(viewModel) {
            navController.navigate(Drafts)
        }
    }
    composable<Profile> {
        val profileViewModel : ProfileViewModel = viewModel(
            factory = ProfileViewModel.Factory(
                appViewModel.user.value!!,
                appViewModel.api,
                appViewModel.database
            )
        )
        // profileViewModel.loadUserRecipes()
        ProfileScreen(
            profileViewModel,
            recipeViewModel,
            navController,
            onEditClick = {
                navController.navigate(EditProfile)
            }
        )
    }
    composable<EditProfile> {
        EditProfileScreen(
            appViewModel.user.value!!,
            onSave = {
                user: User ->
                appViewModel.viewModelScope.launch(){
                    appViewModel.api.updateUser(user)
                }
                appViewModel.user.value = user
                navController.navigate(Profile)
            }

        )
    }
    composable<ViewRecipe> {
        when {
            appViewModel.selectedRecipe == null -> {
                Text("No Recipe Found")
            }
            else -> {
                ViewRecipeScreen(appViewModel.selectedRecipe!!)
            }
        }
    }
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