package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.worldchangingcookingapp.models.User
import com.example.worldchangingcookingapp.viewmodel.UserState


// List of available profile picture URLs for selection
val profilePictures: List<String> = listOf(
    "https://avatar.iran.liara.run/public/85",
    "https://avatar.iran.liara.run/public/95",
    "https://avatar.iran.liara.run/public/75",
    "https://avatar.iran.liara.run/public/65",
    "https://avatar.iran.liara.run/public/55",
    "https://avatar.iran.liara.run/public/45",
    "https://avatar.iran.liara.run/public/35",
    "https://avatar.iran.liara.run/public/18",
    "https://avatar.iran.liara.run/public/28",
    "https://avatar.iran.liara.run/public/38",
    "https://avatar.iran.liara.run/public/48",
    "https://avatar.iran.liara.run/public/58",
    "https://avatar.iran.liara.run/public/68",
    "https://avatar.iran.liara.run/public/78",
    "https://avatar.iran.liara.run/public/88",
)

@Composable
fun EditProfileScreen(userState: UserState, onSave: (User) -> Unit) {
    // Extract user from UserState
    var user = when (userState) {
        is UserState.SignedIn -> userState.user
        else -> null
    }

    // State variables for editing
    var username by remember { mutableStateOf(user?.displayName ?: "example") }
    var profilePicturePath by remember { mutableStateOf(user?.profilePicturePath ?: "") }
    var instagramName by remember { mutableStateOf(user?.instagramName ?: "") }

    // Toggles for opening dialogs
    var showImageSelector by remember { mutableStateOf(false) }
    var showUsernameSelector by remember { mutableStateOf(false) }
    var showInstagramNameSelector by remember { mutableStateOf(false) }

    // Main layout
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Current profile picture preview
        Text("Current Profile Picture :")
        AsyncImage(
            model = profilePicturePath,
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(120.dp).clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Change picture button
        Button(onClick = { showImageSelector = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Change Profile Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display current username
        Text("Current Username :")
        Text(username)

        Spacer(modifier = Modifier.height(16.dp))

        // Change username button
        Button(onClick = { showUsernameSelector = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Change Username")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display current Instagram name
        Text("Current Instagram Pseudo :")
        Text(instagramName)

        Spacer(modifier = Modifier.height(16.dp))

        // Change Instagram pseudo button
        Button(onClick = { showInstagramNameSelector = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Change Instagram Pseudo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save all changes
        Button(
            onClick = {
                user = user?.copy(
                    displayName = username,
                    profilePicturePath = profilePicturePath,
                    instagramName = instagramName
                )
                onSave(user!!)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save changes")
        }
    }

    // Username dialog
    if (showUsernameSelector) {
        StringSelectorPopup(
            currentValue = username,
            onSave = {
                username = it
                showUsernameSelector = false
            },
            onDismiss = { showUsernameSelector = false }
        )
    }

    // Profile picture dialog
    if (showImageSelector) {
        ProfilePictureSelectorPopup(
            onImageSelected = {
                profilePicturePath = it
                showImageSelector = false
            },
            onDismiss = { showImageSelector = false }
        )
    }

    // Instagram pseudo dialog
    if (showInstagramNameSelector) {
        StringSelectorPopupInstagram(
            currentValue = instagramName,
            onSave = {
                instagramName = it
                showInstagramNameSelector = false
            },
            onDismiss = { showInstagramNameSelector = false }
        )
    }
}

@Composable
fun ProfilePictureSelectorPopup(
    onImageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val imageUrls = profilePictures

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column {
                // Close button
                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Close")
                }

                // Grid of profile pictures
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(imageUrls.size) { index ->
                        AsyncImage(
                            model = imageUrls[index],
                            contentDescription = "Avatar $index",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .clickable {
                                    onImageSelected(imageUrls[index])
                                    onDismiss()
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StringSelectorPopup(
    currentValue: String = "",
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(currentValue) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Edit Username", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(currentValue) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(text) }) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}

@Composable
fun StringSelectorPopupInstagram(
    currentValue: String = "",
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(currentValue) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Edit Instagram Pseudo", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(currentValue) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(text) }) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}
