package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.worldchangingcookingapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, onSuccess : () -> Unit) {

    if (viewModel.reload) {
        onSuccess()
    }

    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(start = 24.dp, end = 24.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(12.dp),
            value = viewModel.username,
            onValueChange = {
                viewModel.username = it
            },
            maxLines = 1,
            label = {
                Text("Username")
            }
        )
        OutlinedTextField(
            modifier = Modifier.padding(12.dp),
            value = viewModel.password,
            onValueChange = {
                viewModel.password = it
            },
            maxLines = 1,
            label = {
                Text("Password")
            }
        )
        if (viewModel.creatingAccount) {
            OutlinedTextField(
                modifier = Modifier.padding(12.dp),
                value = viewModel.passwordDupe,
                onValueChange = {
                    viewModel.passwordDupe = it
                },
                maxLines = 1,
                label = {
                    Text("Repeat Password")
                }
            )
        }

        OutlinedButton(
            modifier = Modifier.padding(12.dp),
            onClick = {
                if (viewModel.creatingAccount) {
                    viewModel.createAccount()
                } else {
                    viewModel.login()
                }
            }
        ) {
            val text : String = if (viewModel.creatingAccount) "Create Account" else "Login"
            Text(text)
        }

        TextButton(onClick = {
            viewModel.creatingAccount = !viewModel.creatingAccount
        }) {
            val text : String = if (viewModel.creatingAccount) "Login" else "Create An Account"
            Text(text)
        }
    }
}