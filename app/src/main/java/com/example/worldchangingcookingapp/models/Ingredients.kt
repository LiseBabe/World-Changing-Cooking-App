package com.example.worldchangingcookingapp.models

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        OutlinedCard (modifier = modifier.fillMaxWidth()) {
            Row {
                Text(text = state.value.quantity.toString(), modifier = Modifier.padding(8.dp))
                Text(text = state.value.unit, modifier = Modifier.padding(8.dp))
                Text(text = state.value.name, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

class IngredientItemFactory : ListItemFactory<Ingredients> {
    override fun newListItem(): ListItem<Ingredients> {
        return IngredientItem(mutableStateOf(Ingredients(0.0, "", "", "")))
    }
}