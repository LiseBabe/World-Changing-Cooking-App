package com.example.worldchangingcookingapp.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ch.benlu.composeform.components.TextFieldComponent
import com.example.worldchangingcookingapp.form.fields.ListItem
import com.example.worldchangingcookingapp.form.fields.ListItemFactory
import kotlinx.serialization.Serializable

@Serializable
data class Ingredients(
    var quantity: Double = 0.0,
    var unit: String = "",
    var name: String = "",
    var moreInformation: String = "",
)

class IngredientItem(override var state: MutableState<Ingredients>) : ListItem<Ingredients> {
    @Composable
    override fun ElementBuilder(modifier: Modifier) {

        var isDialogVisible by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isDialogVisible = it.isFocused },
            value = "${state.value.quantity}${state.value.unit} ${state.value.name}",
            readOnly = true,
            onValueChange = {}
        )


        if (isDialogVisible) {
            var content = remember { mutableStateOf(state.value) }
            Dialog(onDismissRequest = {
                state.value = content.value
                isDialogVisible = false
                focusManager.clearFocus()
            }) {
                Surface (
                    modifier = Modifier.width(350.dp),
                    shape = RoundedCornerShape(10.dp)
                )
                {
                    Column (horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "New Ingredient",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(top = 8.dp))
                        OutlinedTextField(
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
                        OutlinedTextField(
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
                        OutlinedTextField(
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
                        OutlinedTextField(
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