package com.example.worldchangingcookingapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.worldchangingcookingapp.models.User


@SuppressLint("UnrememberedMutableState")
@Composable
fun ViewUserScreen(
    user: User,                             // The user to be viewed
    isFriend: Boolean = false,              // Indicates if the current user is already a friend
    onAddFriendClick: (User) -> Unit,       // Callback for adding a friend
    onDeleteFriendClick: (User) -> Unit     // Callback for removing a friend
) {
    val context = LocalContext.current

    // State to control whether the "Follow/Unfollow" button should show as followed
    var displayButton = mutableStateOf(isFriend)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Displays the user's profile picture
        AsyncImage(
            model = user.profilePicturePath,
            contentDescription = "${user.displayName}'s profile picture",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displays the user's name
        Text(
            text = user.displayName ?: "Unknown User",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Follow/Unfollow button
        Button(
            onClick = {
                if (!isFriend) {
                    onAddFriendClick(user)        // Add friend if not already one
                    displayButton.value = true
                } else {
                    onDeleteFriendClick(user)     // Remove friend if already one
                    displayButton.value = true
                }
            },
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (displayButton.value) "Unfollow" else "Follow")
        }
    }
}

