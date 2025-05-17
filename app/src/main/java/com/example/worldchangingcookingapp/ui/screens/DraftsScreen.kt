package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.worldchangingcookingapp.contants.ScreenType
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.viewmodel.DraftsViewModel

@Composable
fun DraftsScreen(viewModel : DraftsViewModel, screenType: ScreenType, onSelect : (recipe : Recipe?) -> Unit) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()

    LazyVerticalStaggeredGrid(
        columns = if (screenType == ScreenType.TALL) StaggeredGridCells.Fixed(1) else StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(recipes) { recipe ->
            RecipeCard(recipe, onSelect) { id ->
                viewModel.deleteDraft(id)
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        onSelect(null)
                    }
                ) {
                    Icon(Icons.Default.Add, "New Draft")
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onSelect : (recipe : Recipe) -> Unit, onDelete : (String) -> Unit) {
    OutlinedCard (modifier = Modifier.fillMaxWidth()) {
        Row (
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = recipe.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.displaySmall
            )
            Row {
                IconButton(
                    onClick = {
                        onSelect(recipe)
                    }
                ) {
                    Icon(Icons.Default.Create, "Edit Draft")
                }
                IconButton(
                    onClick = {
                        onDelete(recipe.id)
                    }
                ) {
                    Icon(Icons.Default.Delete, "Delete Draft")
                }
            }
        }
    }
}