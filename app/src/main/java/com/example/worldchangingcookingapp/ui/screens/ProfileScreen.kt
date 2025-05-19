package com.example.worldchangingcookingapp.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.worldchangingcookingapp.R
import com.example.worldchangingcookingapp.contants.ScreenType
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.viewmodel.ProfileViewModel


/*
 * Main Composable for displaying the user's profile screen.
 * Shows user details, stats (recipes and friends), and allows navigation
 * to edit profile or view Instagram. Also conditionally displays either
 * the user's recipes or friends list based on viewModel state.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,            // ViewModel managing user data and UI state
    screenType: ScreenType,                 // Type of screen to adapt child components
    onEditClick: () -> Unit,                // Callback when "Edit Profile" is clicked
    onRecipeClick: (Recipe) -> Unit,        // Callback when a recipe is selected
    onUser: (String) -> Unit                // Callback when a user is selected (from friends list)
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section: profile image, stats, name, and buttons
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Display user's profile picture
                AsyncImage(
                    model = viewModel.user?.profilePicturePath,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Display recipe and friend statistics
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModel.user?.recipes?.let { recipes ->
                        ProfileStat(
                            label = "Recipe(s)",
                            count = recipes.size,
                            onClick = { viewModel.showListRecipe = true } // Show recipe list
                        )
                    }
                    viewModel.user?.friends?.let { friends ->
                        ProfileStat(
                            label = "Friend(s)",
                            count = friends.size,
                            onClick = { viewModel.showListRecipe = false } // Show friends list
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Display user's name
                Text(
                    text = viewModel.user?.displayName ?: "No User Found",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // "Edit Profile" button
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Instagram profile button
                val instagramName = viewModel.user?.instagramName
                Button(
                    onClick = {
                        if (!instagramName.isNullOrBlank()) {
                            val url = "https://www.instagram.com/$instagramName"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                context,
                                "Instagram username not set. Please edit your profile first.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_instagram),
                        contentDescription = "Instagram icon",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp)
                    )
                    Text("See Instagram profile")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Conditional content: recipe list or friend list based on showListRecipe flag
        item {
            if (viewModel.showListRecipe) {
                if (viewModel.isRecipesLoading) {
                    Text("Loading user's recipes...")
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                    ) {
                        RecipeListScreen(
                            recipes = viewModel.recipes ?: emptyList(),
                            screenType = screenType,
                            onRecipeClick = onRecipeClick,
                            onUserClick = onUser
                        )
                    }
                }
            } else {
                if (viewModel.isFriendsLoading) {
                    Text("Loading user's friends...")
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                    ) {
                        UserListScreen(userList = viewModel.friends ?: emptyList())
                    }
                }
            }
        }
    }
}

/*
 * Composable for displaying a single profile statistic (e.g., number of recipes or friends).
 * Clicking on it triggers a callback that updates which list is displayed.
 */
@Composable
fun ProfileStat(label: String, count: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = count.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = label)
    }
}
