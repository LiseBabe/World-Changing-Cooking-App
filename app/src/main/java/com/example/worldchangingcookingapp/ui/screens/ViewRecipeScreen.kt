package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.worldchangingcookingapp.models.CookingType
import com.example.worldchangingcookingapp.models.Difficulty
import com.example.worldchangingcookingapp.models.Ingredients
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.TypeOfRecipe
import kotlin.time.Duration.Companion.minutes
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ViewRecipeScreen(recipe : Recipe) {

    Column (
        modifier = Modifier.padding(start = 8.dp)
    ){
        //Poster Here
        Text(recipe.title, style = MaterialTheme.typography.headlineLarge)
        Text("By ${recipe.authorName}", style = MaterialTheme.typography.labelSmall)
        val formattedDate = recipe.publicationDate?.toDate()?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: "10/06/2009"
        Text("The ${formattedDate}", style = MaterialTheme.typography.labelSmall)
        //add interaction buttons here (saving, favoriting, liking, etc)
        Spacer(Modifier.padding(8.dp))
        Text(recipe.description, style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.padding(8.dp))

        Row (horizontalArrangement = Arrangement.SpaceAround) {
            Card (Modifier.fillMaxWidth().padding(start = 8.dp, end = 4.dp).weight(1f)) {
                Column {
                    Spacer(Modifier.padding(4.dp))
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.Warning, contentDescription = "Warning")
                        Text(recipe.difficulty.name.replace('_', ' '), style = MaterialTheme.typography.bodyLarge)
                    }
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.ShoppingCart, contentDescription = "Price")
                        Text(recipe.price.name, style = MaterialTheme.typography.bodyLarge)
                    }
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Filled.Person, contentDescription = "Number of People")
                        Text(recipe.numberOfPeople.toString(), style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(Modifier.padding(4.dp))
                }
            }
            Card (Modifier.fillMaxWidth().padding(start = 8.dp, end = 4.dp).weight(1f)) {
                Column {
                    Spacer(Modifier.padding(4.dp))
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.DateRange, contentDescription = "Prep Time")
                        val totalSeconds = recipe.preparationTime / 1000
                        val hours = totalSeconds / 3600
                        val minutes = (totalSeconds % 3600) / 60
                        val seconds = totalSeconds % 60

                        val formattedTime = if (hours > 0) {
                            "${hours}h ${minutes}min"
                        } else {
                            "${minutes}min ${seconds}s"
                        }

                        Text("Prep $formattedTime", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.DateRange, contentDescription = "Cook Time")
                        val totalSeconds = recipe.cookingTime / 1000
                        val hours = totalSeconds / 3600
                        val minutes = (totalSeconds % 3600) / 60
                        val seconds = totalSeconds % 60

                        val formattedTime = if (hours > 0) {
                            "${hours}h ${minutes}min"
                        } else {
                            "${minutes}min ${seconds}s"
                        }

                        Text("Cook $formattedTime", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.DateRange, contentDescription = "Rest Time")
                        val totalSeconds = recipe.restingTime / 1000
                        val hours = totalSeconds / 3600
                        val minutes = (totalSeconds % 3600) / 60
                        val seconds = totalSeconds % 60

                        val formattedTime = if (hours > 0) {
                            "${hours}h ${minutes}min"
                        } else {
                            "${minutes}min ${seconds}s"
                        }

                        Text("Rest $formattedTime", style = MaterialTheme.typography.bodyLarge)

                    }
                    Spacer(Modifier.padding(4.dp))
                }
            }
        }
        Spacer(Modifier.padding(8.dp))
        Text("Ingredients", style = MaterialTheme.typography.headlineSmall)
        Column {
            recipe.ingredients.forEach {
                Row {
                    Text(it.quantity.toString())
                    Text(" ${it.unit}")
                    Text(" ${it.name}")
                }
            }
        }

        Spacer(Modifier.padding(8.dp))

        Text("Steps", style = MaterialTheme.typography.headlineSmall)
        Column {
            recipe.steps.forEachIndexed { i, step ->
                Card (modifier = Modifier.defaultMinSize(minWidth = 200.dp).padding(start = 8.dp, bottom = 4.dp)) {
                    Column {
                        Text("Step ${i + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 8.dp))
                        Text(step,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
        Spacer(Modifier.padding(4.dp))
        Text("Additional Info", style = MaterialTheme.typography.headlineSmall)
        Text(recipe.moreInformation, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeScreen() {
    ViewRecipeScreen(
        Recipe(
            id = "",
            title = "Banana Bread",
            authorId = "",
            authorName = "Jeremy Beremy",
            authorProfilePath = "",
            description = "A delicious banana bread recipe. Beginner friendly. Milk Free.",
            difficulty = Difficulty.REALLY_EASY,
            price = Price.EXPENSIVE,
            typeOfRecipe = TypeOfRecipe.DESSERT,
            numberOfPeople = 4,
            preparationTime = 20008700L,
            cookingTime = 45L,
            restingTime = 15L,
            cookingType = CookingType.OVEN,
            ingredients = listOf(
                Ingredients(40.0, "mg", "butter", "")
            ),
            steps = listOf(
                "Make banana bread."
            ),
            moreInformation = "Don't make any mistakes."
        )
    )
}