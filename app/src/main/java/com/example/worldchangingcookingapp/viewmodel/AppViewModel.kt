package com.example.worldchangingcookingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.worldchangingcookingapp.WorldChangingCookingApplication
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.services.AccountService
import com.example.worldchangingcookingapp.services.ApiService
import com.example.worldchangingcookingapp.services.DatabaseService
import kotlinx.coroutines.launch

/*
 * User State object. Used in most pages of the app to provide
 * the user object.
 */
sealed interface UserState {
    data class SignedIn(val user: User) : UserState
    object SignedOut : UserState
    object Loading : UserState
    object Error : UserState
}
/*
 * Handles all state that must be maintained globally throughout the app.
 * Stores different services for delegation to lower scoped viewModels.
 */
class AppViewModel (
    val auth: AccountService,
    val api: ApiService,
    val database: DatabaseService
) : ViewModel() {

    var loggedIn by mutableStateOf(auth.hasUser)
    var user : UserState by mutableStateOf(UserState.SignedOut)

    var selectedRecipe by mutableStateOf<Recipe?>(null)

    var selectedUser by mutableStateOf<User?>(null)

    init {
        signIn()
    }

    /*
     * Called at the start of the app to get the logged in users
     * information. If this fails client will be redirected to
     * login screen.
     */
    fun signIn() {
        viewModelScope.launch {
            user = UserState.Loading
            user = try {
                UserState.SignedIn(api.getUser(auth.currentUserId)!!)
            } catch (e : Exception) {
                UserState.Error
            }
            loggedIn = auth.hasUser
        }
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut()
            loggedIn = auth.hasUser
            user = UserState.SignedOut
        }
    }

    fun setSelectedUser(id: String){
        viewModelScope.launch {
            selectedUser =
                api.getUser(id)
        }
    }

    fun addFriend(id: String){
        val userCopy = user
        when (userCopy) {
            is UserState.SignedIn -> {
                viewModelScope.launch {
                    api.addFriend(userCopy.user,id)
                }
            }
            else -> null
        }
    }

    fun removeFriend(id: String){
        val userCopy = user
        when (userCopy) {
            is UserState.SignedIn -> {
                viewModelScope.launch {
                    api.removeFriend(userCopy.user,id)
                }
            }
            else -> null
        }
    }

    fun isFriend(id: String): Boolean {
        return when (val userCopy = user) {
            is UserState.SignedIn -> {
                userCopy.user.friends.contains(id) ?: false
            }
            else -> false
        }
    }

    fun refreshUser() {
        viewModelScope.launch {
            user = UserState.Loading
            user = try {
                UserState.SignedIn(api.getUser(auth.currentUserId)!!)
            } catch (e: Exception) {
                UserState.Error
            }
        }
    }

    companion object {
        val Factory =
            viewModelFactory {
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WorldChangingCookingApplication)
                    val accountService = application.container.authService
                    val apiService = application.container.apiService
                    val databaseService = application.container.databaseService
                    AppViewModel(accountService, apiService, databaseService)
                }
            }
    }
}