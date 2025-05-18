package com.example.worldchangingcookingapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.worldchangingcookingapp.R
import com.example.worldchangingcookingapp.contants.ScreenType
import com.example.worldchangingcookingapp.data.FakeRecipeDatabase
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.database.Users
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
    screenType: ScreenType,
    onEditClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onUser: (String) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = viewModel.user?.profilePicturePath,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModel.user?.recipes?.let { recipes ->
                        ProfileStat(
                            label = "Recipe(s)",
                            count = recipes.size,
                            onClick = { viewModel.showListRecipe = true }
                        )
                    }
                    viewModel.user?.friends?.let { friends ->
                        ProfileStat(
                            label = "Friend(s)",
                            count = friends.size,
                            onClick = { viewModel.showListRecipe = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = viewModel.user?.displayName ?: "No User Found",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }

                Spacer(modifier = Modifier.height(16.dp))

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


