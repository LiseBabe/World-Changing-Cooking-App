package com.example.worldchangingcookingapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController, screenType: ScreenType, onEditClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = viewModel.user?.profilePicturePath,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.user?.recipes?.let { ProfileStat("Recipe(s)", it.size) }
            viewModel.user?.friends?.let { ProfileStat("Friend(s)", it.size) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = viewModel.user?.displayName?: "No User Found" , fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Edit Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
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

        if (viewModel.isRecipesLoading) {
            Text("Loading user's recipes...")
        } else {
            RecipeListScreen(
                recipes = viewModel.recipes!!,
                screenType,
                onRecipeClick = {
                    print("do something with the recipe")
                    //navController.navigate("recipeDetail")
                }
            )
        }

    }
}

@Composable
fun ProfileStat(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, fontSize = 14.sp)
    }
}


