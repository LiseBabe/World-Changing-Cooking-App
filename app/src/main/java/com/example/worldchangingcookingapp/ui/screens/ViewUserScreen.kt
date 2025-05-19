package com.example.worldchangingcookingapp.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.worldchangingcookingapp.R
import com.example.worldchangingcookingapp.models.User


@SuppressLint("UnrememberedMutableState")
@Composable
fun ViewUserScreen(
    user: User,
    isFriend: Boolean = false,
    onAddFriendClick: (User) -> Unit,
    onDeleteFriendClick: (User) -> Unit
) {
    val context = LocalContext.current
    var displayButton = mutableStateOf(isFriend)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.profilePicturePath,
            contentDescription = "${user.displayName}'s profile picture",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.displayName ?: "Unknown User",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isFriend) {
                    onAddFriendClick(user)
                    displayButton.value = true
                } else {
                    onDeleteFriendClick(user)
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
