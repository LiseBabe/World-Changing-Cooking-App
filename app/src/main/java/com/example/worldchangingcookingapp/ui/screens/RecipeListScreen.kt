package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.worldchangingcookingapp.contants.ScreenType
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.User
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun RecipeListScreen(recipes: List<Recipe>, screenType: ScreenType, onRecipeClick: (Recipe) -> Unit, onUserClick: (String) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = if (screenType == ScreenType.TALL) StaggeredGridCells.Fixed(1) else StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        items(recipes) { recipe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
                    .clickable { onRecipeClick(recipe) },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    //Title
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Author
                    Text(
                        text = "By ${recipe.authorName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable {
                            onUserClick(recipe.authorId)
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Other information
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        val totalMillis = recipe.preparationTime
                        if (totalMillis < 60000){
                            val seconds = totalMillis / 1000
                            //val formattedTime = "${seconds}s"
                            Text("🕒 ${seconds}s")
                        } else {
                            val totalMinutes = totalMillis / 1000 / 60
                            val hours = totalMinutes / 60
                            val minutes = totalMinutes % 60

                            val formattedTime = buildString {
                                if (hours > 0) append("${hours}h ")
                                append("${minutes}min")
                            }
                            Text("🕒 $formattedTime")
                        }
                        Icon(Icons.Rounded.Warning, contentDescription = "Warning")
                        Text(recipe.difficulty.name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() })
                        Icon(Icons.Rounded.ShoppingCart, contentDescription = "Price")
                        Text(
                            when (recipe.price) {
                                Price.CHEAP -> "Cheap"
                                Price.MODERATE -> "Moderate"
                                Price.EXPENSIVE -> "Expensive"
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}