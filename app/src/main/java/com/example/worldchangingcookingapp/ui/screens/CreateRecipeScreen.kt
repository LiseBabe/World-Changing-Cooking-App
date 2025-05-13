package com.example.worldchangingcookingapp.ui.screens

import android.content.Context
import android.view.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.worldchangingcookingapp.viewmodel.RecipeFormViewModel
import com.example.worldchangingcookingapp.form.StepItemFactory
import com.example.worldchangingcookingapp.form.fields.DurationField
import com.example.worldchangingcookingapp.form.fields.LargeTextField
import com.example.worldchangingcookingapp.form.fields.ListField
import com.example.worldchangingcookingapp.form.fields.PickerField
import com.example.worldchangingcookingapp.form.fields.TextField
import com.example.worldchangingcookingapp.models.CookingType
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.Difficulty
import com.example.worldchangingcookingapp.models.IngredientItemFactory
import com.example.worldchangingcookingapp.models.Ingredients
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.TypeOfRecipe
import com.example.worldchangingcookingapp.viewmodel.PostResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

fun blankRecipe(): Recipe {
    return Recipe(
        title = "",
        authorId = "",
        authorName = "",
        authorProfilePath = "",
        description = "",
        difficulty = Difficulty.MEDIUM,
        price = Price.MODERATE,
        typeOfRecipe = TypeOfRecipe.MAIN_COURSE,
        numberOfPeople = 1,
        preparationTime = 0.minutes,
        cookingTime = 0.minutes,
        restingTime = 0.minutes,
        cookingType = CookingType.NO_COOKING,
        ingredients = arrayListOf<Ingredients>(),
        steps = arrayListOf<String>(),
        moreInformation = "",
    )
}

@Composable
fun CreateRecipeScreen(formModel : RecipeFormViewModel = viewModel(), recipe: Recipe = blankRecipe()) {

    var recipe by remember { mutableStateOf(recipe) }
    var isPreviewVisible by remember { mutableStateOf(false) }
    var isResultsVisible by remember { mutableStateOf(false) }
    var resultTitle by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        formModel.postStatus.collect { result ->
            when (result) {
                is PostResult.Success -> {
                    resultTitle = "Success"
                    resultMessage = "Recipe Published!"
                    isResultsVisible = true
                    formModel.form.clear()
                    recipe = blankRecipe()
                }
                is PostResult.Error -> {
                    resultTitle = "Error"
                    resultMessage = result.message
                    isResultsVisible = true
                }
            }
        }
    }



    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(8.dp)
    ) {
        TextField(
            label = "Title",
            form = formModel.form,
            fieldState = formModel.form.title
        ).Field()

        LargeTextField(
            label = "Description",
            form = formModel.form,
            fieldState = formModel.form.description,
            popUpTitle = "Write Description"
        ).Field()

        PickerField(
            label = "Difficulty",
            form = formModel.form,
            fieldState = formModel.form.difficulty,
            isSearchable = false
        ).Field()

        PickerField(
            label = "Price",
            form = formModel.form,
            fieldState = formModel.form.price,
            isSearchable = false
        ).Field()

        PickerField(
            label = "Recipe Type",
            form = formModel.form,
            fieldState = formModel.form.typeOfRecipe,
            isSearchable = false
        ).Field()

        PickerField(
            label = "# of People",
            form = formModel.form,
            fieldState = formModel.form.numberOfPeople,
            isSearchable = false
        ).Field()

        PickerField(
            label = "Cooking Type",
            form = formModel.form,
            fieldState = formModel.form.cookingType,
            isSearchable = false
        ).Field()

        DurationField(
            label = "Preparation Time",
            form = formModel.form,
            fieldState = formModel.form.preparationTime
        ).Field()

        DurationField(
            label = "Cooking Time",
            form = formModel.form,
            fieldState = formModel.form.cookingTime
        ).Field()

        DurationField(
            label = "Resting Time",
            form = formModel.form,
            fieldState = formModel.form.restingTime
        ).Field()

        ListField(
            label = "Ingredients",
            form = formModel.form,
            fieldState = formModel.form.ingredients,
            itemFactory = IngredientItemFactory()
        ).Field()

        ListField(
            label = "Steps",
            form = formModel.form,
            fieldState = formModel.form.steps,
            itemFactory = StepItemFactory()
        ).Field()

        LargeTextField(
            label = "Additional Info",
            form = formModel.form,
            fieldState = formModel.form.moreInformation,
            popUpTitle = "Additional Info"
        ).Field()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 24.dp,
                    end = 24.dp
                )
            ) {
                Text("Discard")
            }
            Spacer(Modifier.padding(12.dp))
            OutlinedButton(
                onClick = {
                    recipe = formModel.getRecipe()
                    isPreviewVisible = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 24.dp,
                    end = 24.dp
                )
            ) {
                Text("Preview")
            }
            Spacer(Modifier.padding(12.dp))
            OutlinedButton(
                onClick = {
                    formModel.publishRecipe()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 24.dp,
                    end = 24.dp
                )
            ) {
                Text("Publish")
            }
        }
    }
    if (isPreviewVisible) {
        PreviewRecipePopup(recipe) {
            isPreviewVisible = false
        }
    }
    if (isResultsVisible) {
        PublishResultDialog(resultTitle, resultMessage) {
            isResultsVisible = false
        }
    }
}

@Composable
fun PublishResultDialog(title: String, message: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {
        onDismiss.invoke()
    }) {
        Surface {
            Column (
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title, style = MaterialTheme.typography.headlineSmall)
                Text(message, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                OutlinedButton(
                    onClick = onDismiss
                ) {
                    Text("Ok")
                }
            }
        }
    }
}

@Composable
fun PreviewRecipePopup(recipe: Recipe, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {
        onDismiss.invoke()
    },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )) {
        Surface (
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp).padding(start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column (verticalArrangement = Arrangement.SpaceBetween){
                LazyColumn (modifier = Modifier.weight(1f)) {
                    item {
                        ViewRecipeScreen(recipe)
                    }
                }
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    OutlinedButton(
                        onClick = {
                            onDismiss.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
                    ) {
                        Text("Close")
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateRecipePreview() {
    CreateRecipeScreen()
}

