package com.example.worldchangingcookingapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.benlu.composeform.fields.PickerField
import ch.benlu.composeform.fields.TextField
import com.example.worldchangingcookingapp.form.RecipeFormViewModel
import com.example.worldchangingcookingapp.form.StepItemFactory
import com.example.worldchangingcookingapp.form.fields.DurationField
import com.example.worldchangingcookingapp.form.fields.ListField
import com.example.worldchangingcookingapp.models.CookingType
import com.example.worldchangingcookingapp.models.Recipe
import com.example.worldchangingcookingapp.models.Difficulty
import com.example.worldchangingcookingapp.models.IngredientItem
import com.example.worldchangingcookingapp.models.IngredientItemFactory
import com.example.worldchangingcookingapp.models.Ingredients
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.TypeOfRecipe
import com.example.worldchangingcookingapp.ui.theme.WorldChangingCookingAppTheme
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

fun blankRecipe(): Recipe {
    return Recipe(
        id = UUID.randomUUID(),
        title = "",
        publicationDate = "",
        difficulty = Difficulty.MEDIUM,
        price = Price.MODERATE,
        typeOfRecipe = TypeOfRecipe.MAIN_COURSE,
        numberOfPeople = 1,
        preparationTime = 0.minutes,
        cookingTime = 0.minutes,
        restingTime = 0.minutes,
        cookingType = CookingType.NO_COOKING,
        posterPath = "",
        ingredients = arrayListOf<Ingredients>(),
        steps = arrayListOf<String>(),
        moreInformation = "",
    )
}

@Composable
fun CreateRecipeScreen(formModel : RecipeFormViewModel, recipe: Recipe = blankRecipe()) {
    Column (
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(8.dp)
        ){
        TextField(
            label = "Title",
            form = formModel.form,
            fieldState = formModel.form.title
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

    }
}


@Preview
@Composable
fun CreateRecipePreview() {
    CreateRecipeScreen(RecipeFormViewModel())
}

