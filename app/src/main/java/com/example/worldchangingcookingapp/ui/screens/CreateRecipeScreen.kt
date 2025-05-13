package com.example.worldchangingcookingapp.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.worldchangingcookingapp.form.RecipeFormViewModel
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
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

fun blankRecipe(): Recipe {
    return Recipe(
        id = "",
        title = "",
        authorId = "",
        authorName = "",
        authorProfilePath = "",
        description = "",
        publicationDate = "",
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
                    recipe = formModel.form.toRecipe(recipe)
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
}

@Composable
fun PreviewRecipePopup(recipe: Recipe, onDismiss: () -> Unit) {
    println("** Popup Shown **")
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

