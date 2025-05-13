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

@Composable
fun ViewRecipeScreen(recipe : Recipe) {

    Column {
        //Poster Here

        Text(recipe.title, style = MaterialTheme.typography.headlineLarge)
        Text("By ${recipe.authorName}", style = MaterialTheme.typography.labelSmall)
        Text(recipe.publicationDate?.toString() ?: "10/9/2025", style = MaterialTheme.typography.labelSmall)
        //add interaction buttons here (saving, favoriting, liking, etc)
        Spacer(Modifier.padding(8.dp))
        Text(recipe.description, style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.padding(8.dp))

        Row (horizontalArrangement = Arrangement.SpaceAround) {
            Card (Modifier.fillMaxWidth().padding(start = 8.dp, end = 4.dp).weight(1f)) {
                Column {
                    Spacer(Modifier.padding(4.dp))
                    Row {
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.Warning, contentDescription = "Warning")
                        Text(recipe.difficulty.name, style = MaterialTheme.typography.bodyLarge)
                    }
                    Row {
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.ShoppingCart, contentDescription = "Price")
                        Text(recipe.price.name, style = MaterialTheme.typography.bodyLarge)
                    }
                    Row {
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
                    Row {
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.DateRange, contentDescription = "Prep Time")
                        Text("Prep ${recipe.preparationTime}", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row {
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.DateRange, contentDescription = "Cook Time")
                        Text("Cook ${recipe.cookingTime}", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row {
                        Spacer(Modifier.padding(4.dp))
                        Icon(Icons.Rounded.DateRange, contentDescription = "Rest Time")
                        Text("Rest ${recipe.restingTime}", style = MaterialTheme.typography.bodyLarge)
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
                    Text(it.unit)
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
            difficulty = Difficulty.EASY,
            price = Price.MODERATE,
            typeOfRecipe = TypeOfRecipe.DESSERT,
            numberOfPeople = 4,
            preparationTime = 20.minutes,
            cookingTime = 45.minutes,
            restingTime = 15.minutes,
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