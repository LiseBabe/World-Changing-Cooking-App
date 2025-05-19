package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worldchangingcookingapp.R
import com.example.worldchangingcookingapp.models.User

// Composable that displays a vertical list of users using LazyColumn
@Composable
fun UserListScreen(
    userList: List<User>,          // List of users to display
    modifier: Modifier = Modifier  // Optional modifier for customization
) {
    LazyColumn(modifier = modifier) {
        // For each user in the list, display a user item card
        items(userList) { user ->
            UserItemCard(
                profileImageRes = R.drawable.profile_pic, // Static placeholder image
                user = user
            )
        }
    }
}

// Composable that defines how each user is displayed in the list
@Composable
fun UserItemCard(
    profileImageRes: Int,  // Resource ID for the profile image
    user: User,            // User data to display
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp) // Padding around each item
    ) {
        // Profile image (currently static)
        Image(
            painter = painterResource(id = profileImageRes),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp)) // Space between image and text

        // User details: name, ID, follower count, recipe count
        Column {
            Text(
                text = user.displayName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "@${user.id}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${user.friends.size} friends",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${user.recipes.size} recipes",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

