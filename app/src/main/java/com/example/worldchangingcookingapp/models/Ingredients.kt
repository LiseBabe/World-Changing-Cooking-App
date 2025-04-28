package com.example.worldchangingcookingapp.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.worldchangingcookingapp.form.fields.ListItem
import com.example.worldchangingcookingapp.form.fields.ListItemFactory

data class Ingredients(
    var quantity: Double,
    var unit: String,
    var name: String,
    var moreInformation: String,
)

class IngredientItem(override var state: MutableState<Ingredients>) : ListItem<Ingredients> {
    @Composable
    override fun ElementBuilder(modifier: Modifier) {

        var isDialogVisible by remember { mutableStateOf(false) }

        OutlinedCard (
            onClick = { isDialogVisible = !isDialogVisible },
            modifier = modifier.fillMaxWidth()
        ) {
            Row {
                Text(text = state.value.quantity.toString(), modifier = Modifier.padding(8.dp))
                Text(text = state.value.unit, modifier = Modifier.padding(8.dp))
                Text(text = state.value.name, modifier = Modifier.padding(8.dp))
            }
        }

        if (isDialogVisible) {
            var content = remember { mutableStateOf(state.value) }
            Dialog(onDismissRequest = {
                state.value = content.value
                isDialogVisible = false
            }) {
                Surface (
                    modifier = Modifier.width(350.dp).height(450.dp),
                    shape = RoundedCornerShape(10.dp)
                )
                {
                    Column (horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "New Ingredient",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(top = 8.dp))
                        TextField(
                            value = content.value.name,
                            label = { Text(text = "Name") },
                            singleLine = true,
                            onValueChange = { content.value =
                                Ingredients(content.value.quantity,
                                content.value.unit,
                                it,
                                content.value.moreInformation)
                            }
                        )
                        TextField(
                            value = content.value.unit,
                            label = { Text(text = "Unit") },
                            singleLine = true,
                            onValueChange = { content.value =
                                Ingredients(content.value.quantity,
                                    it,
                                    content.value.name,
                                    content.value.moreInformation)
                            }
                        )
                        TextField(
                            value = content.value.quantity.toString(),
                            label = { Text(text = "Quantity") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                val double = try {
                                    it.trim().toDouble()
                                } catch (e : NumberFormatException) {
                                    0.0
                                }
                                content.value =
                                Ingredients(double,
                                    content.value.unit,
                                    content.value.name,
                                    content.value.moreInformation)
                            }
                        )
                        TextField(
                            value = content.value.moreInformation,
                            label = { Text(text = "Addition Information") },
                            singleLine = true,
                            onValueChange = { content.value =
                                Ingredients(content.value.quantity,
                                    content.value.unit,
                                    content.value.name,
                                    it)
                            }
                        )
                    }

                }

            }
        }
    }
}

class IngredientItemFactory : ListItemFactory<Ingredients> {
    override fun newListItem(): ListItem<Ingredients> {
        return IngredientItem(mutableStateOf(Ingredients(0.0, "", "", "")))
    }
}