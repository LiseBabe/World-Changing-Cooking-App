package com.example.worldchangingcookingapp.form.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LargeTextPopup(initialValue: String, callBack: (result: String) -> Unit, title: String = "") {
    var content = remember { mutableStateOf(initialValue) }
    Dialog(onDismissRequest = {
        callBack(content.value)
    }) {
        Surface (
            modifier = Modifier
                .width(350.dp)
                .height(450.dp),
            shape = RoundedCornerShape(10.dp)
        )
        {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 8.dp))
                OutlinedTextField(
                    value = content.value,
                    onValueChange = { content.value = it },
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                        .height(400.dp)
                )
            }
        }
    }
}