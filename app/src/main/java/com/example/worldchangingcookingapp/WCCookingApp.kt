package com.example.worldchangingcookingapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.worldchangingcookingapp.contants.AppBarType
import com.example.worldchangingcookingapp.contants.CreateRecipe
import com.example.worldchangingcookingapp.contants.Drafts
import com.example.worldchangingcookingapp.contants.EditProfile
import com.example.worldchangingcookingapp.contants.Home
import com.example.worldchangingcookingapp.contants.Login
import com.example.worldchangingcookingapp.contants.Profile
import com.example.worldchangingcookingapp.contants.ScreenType
import com.example.worldchangingcookingapp.contants.ViewRecipe
import com.example.worldchangingcookingapp.contants.ViewUser
import com.example.worldchangingcookingapp.contants.topLevelRoutes
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.ui.screens.CreateRecipeScreen
import com.example.worldchangingcookingapp.ui.screens.DraftsScreen
import com.example.worldchangingcookingapp.ui.screens.EditProfileScreen
import com.example.worldchangingcookingapp.ui.screens.LoginScreen
import com.example.worldchangingcookingapp.ui.screens.ProfileScreen
import com.example.worldchangingcookingapp.ui.screens.HomePageScreen
import com.example.worldchangingcookingapp.ui.screens.ViewRecipeScreen
import com.example.worldchangingcookingapp.ui.screens.ViewUserScreen
import com.example.worldchangingcookingapp.ui.theme.WorldChangingCookingAppTheme
import com.example.worldchangingcookingapp.viewmodel.AppViewModel
import com.example.worldchangingcookingapp.viewmodel.DraftsViewModel
import com.example.worldchangingcookingapp.viewmodel.HomePageViewModel
import com.example.worldchangingcookingapp.viewmodel.LoginViewModel
import com.example.worldchangingcookingapp.viewmodel.ProfileViewModel
import com.example.worldchangingcookingapp.viewmodel.RecipeFormViewModel
import kotlinx.coroutines.launch
import com.example.worldchangingcookingapp.viewmodel.UserState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size



@Composable
fun WCCookingApp(
    navController : NavHostController = rememberNavController(),
    windowSize: WindowSizeClass
) {
    val appViewModel : AppViewModel = viewModel(
        factory = AppViewModel.Factory
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route


    val screenType: ScreenType = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            ScreenType.TALL
        }
        else -> {
            ScreenType.WIDE
        }
    }
    val appBarType: AppBarType = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            AppBarType.RAIL
        }
        else -> {
            AppBarType.REGULAR
        }
    }

    val canNavigateBack = navController.previousBackStackEntry != null && currentScreen != null &&
            !topLevelRoutes.any { route -> route.route::class.qualifiedName == currentScreen }

    WorldChangingCookingAppTheme {

        Surface (color = MaterialTheme.colorScheme.background) {
            Scaffold (
                    topBar = { if (appViewModel.loggedIn) TopBar(
                        navigateUp = { navController.navigateUp() },
                        canNavigateBack = canNavigateBack,
                        onSignOut = { appViewModel.signOut() }
                    ) },
                    bottomBar = { if (appViewModel.loggedIn && appBarType == AppBarType.REGULAR) { BottomNavBar(navController) } }
            ){
                innerPadding ->
                    when (appViewModel.user) {
                        is UserState.SignedIn ->
                            Row {
                                if (appBarType == AppBarType.RAIL) {
                                    Rail(
                                        navController = navController,
                                        navigateUp = { navController.navigateUp() },
                                        canNavigateBack = canNavigateBack
                                    )
                                }
                                NavHost(
                                    navController = navController,
                                    startDestination = Home,
                                    modifier = Modifier.padding(innerPadding)
                                ) {
                                    appGraph(navController, appViewModel, screenType)
                                }
                            }
                        is UserState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Refresh, "Loading")
                            }
                        }

                        else -> {
                            val viewModel: LoginViewModel = viewModel(
                                factory = LoginViewModel.Factory(
                                    appViewModel.auth,
                                    appViewModel.api
                                )
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
fun Rail(
    navController : NavHostController,
    navigateUp : () -> Unit,
    canNavigateBack: Boolean
) {
    NavigationRail (
        modifier = Modifier
            .fillMaxHeight(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        if (canNavigateBack) {
            IconButton(onClick = navigateUp, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack,
                    "Go Back",
                    modifier = Modifier.size(28.dp))
            }
        }
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationRailItem(
                icon = { Icon(topLevelRoute.icon, topLevelRoute.label, modifier = Modifier.size(30.dp)) },
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
        Spacer(modifier = Modifier.weight(1f))
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
fun TopBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onSignOut: () -> Unit) {
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
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Go Back"
                    )
                }
            }
        }
    )
}


fun NavGraphBuilder.appGraph(navController : NavController, appViewModel : AppViewModel, screenType: ScreenType) {
    composable<Home> {
        val homePageViewModel: HomePageViewModel = viewModel(
            factory = HomePageViewModel.Factory(
                appViewModel.user,
                appViewModel.api
            )
        )

        HomePageScreen(
            homePageViewModel = homePageViewModel,
            screenType = screenType,
            onSelect = {
                appViewModel.selectedRecipe = it
                navController.navigate(ViewRecipe)
            },
            onUser = {
                appViewModel.setSelectedUser(it)
                navController.navigate(ViewUser)
            }
        )
    }
    composable<Drafts> {
        val viewModel: DraftsViewModel = viewModel(
            factory = DraftsViewModel.Factory(appViewModel.database)
        )
        DraftsScreen(viewModel, screenType) { recipe ->
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
                appViewModel.user,
                appViewModel.api,
                appViewModel.database
            )
        )
        ProfileScreen(
            profileViewModel,
            navController,
            screenType,
            onEditClick = {
                navController.navigate(EditProfile)
            },
            onRecipeClick = {
                appViewModel.selectedRecipe = it
                navController.navigate(ViewRecipe)
            },
            onUser = {
                appViewModel.setSelectedUser(it)
                navController.navigate(ViewUser)
            }
        )
    }
    composable<EditProfile> {
        EditProfileScreen(
            appViewModel.user,
            onSave = {
                user: User ->
                appViewModel.viewModelScope.launch(){
                    appViewModel.api.updateUser(user)
                }
                appViewModel.user = UserState.SignedIn(user)
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
    composable<ViewUser> {
        when {
            appViewModel.selectedUser == null -> {
                Text("No User Found")
            }
            else -> {
                ViewUserScreen(
                    appViewModel.selectedUser!!,
                    appViewModel.isFriend(appViewModel.selectedUser!!.id!!),
                    {appViewModel.addFriend(it.id!!)}
                )
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
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        })
    }
}