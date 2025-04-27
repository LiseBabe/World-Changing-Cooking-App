package com.example.worldchangingcookingapp.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.benlu.composeform.*
import ch.benlu.composeform.fields.PickerValue
import ch.benlu.composeform.validators.MinLengthValidator
import ch.benlu.composeform.validators.NotEmptyValidator
import com.example.worldchangingcookingapp.form.fields.ListItem
import com.example.worldchangingcookingapp.form.fields.ListItemFactory
import com.example.worldchangingcookingapp.models.CookingType
import com.example.worldchangingcookingapp.models.CookingTypePicker
import com.example.worldchangingcookingapp.models.Difficulty
import com.example.worldchangingcookingapp.models.DifficultyPicker
import com.example.worldchangingcookingapp.models.IngredientItem
import com.example.worldchangingcookingapp.models.Ingredients
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.PricePicker
import com.example.worldchangingcookingapp.models.RecipeTypePicker
import com.example.worldchangingcookingapp.models.TypeOfRecipe

data class NumberPicker (
    val value: Int
): PickerValue() {
    override fun searchFilter(query: String): Boolean {
        return this.value.toString().startsWith(query)
    }
}

class StepItem(override var state: MutableState<String>) : ListItem<String> {
    @Composable
    override fun ElementBuilder(modifier: Modifier) {
        OutlinedCard (modifier = modifier.fillMaxWidth()) {
            Text (text = state.value, modifier = Modifier.padding(8.dp))
        }
    }
}

class StepItemFactory : ListItemFactory<String> {
    override fun newListItem(): ListItem<String> {
        return StepItem(mutableStateOf(""))
    }
}

class RecipeForm : Form() {
    override fun self(): Form {
        return this
    }

    @FormField
    val title = FieldState(
        state = mutableStateOf(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            MinLengthValidator(
                minLength = 3,
                errorText = "Must be 3 characters long!"
            )
        )
    )
    @FormField
    val difficulty = FieldState(
        state = mutableStateOf(null),
        options = mutableListOf(
            DifficultyPicker(Difficulty.REALLY_EASY, "Really Easy"),
            DifficultyPicker(Difficulty.EASY, "Easy"),
            DifficultyPicker(Difficulty.MEDIUM, "Medium"),
            DifficultyPicker(Difficulty.HARD, "Hard"),
            DifficultyPicker(Difficulty.REALLY_HARD, "Really Hard"),
        ),
        optionItemFormatter = { "${it?.name}" },
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )
    @FormField
    val price = FieldState(
        state = mutableStateOf(null),
        options = mutableListOf(
            PricePicker(Price.CHEAP, "Cheap"),
            PricePicker(Price.MODERATE, "Moderate"),
            PricePicker(Price.EXPENSIVE, "Expensive")
        ),
        optionItemFormatter = { "${it?.name}" },
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )
    @FormField
    val typeOfRecipe = FieldState(
        state = mutableStateOf(null),
        options = mutableListOf(
            RecipeTypePicker(TypeOfRecipe.MAIN_COURSE, "Main Course"),
            RecipeTypePicker(TypeOfRecipe.STARTER, "Starter"),
            RecipeTypePicker(TypeOfRecipe.CONFECTIONERY, "Confectionery"),
            RecipeTypePicker(TypeOfRecipe.APPETIZER, "Appetizer"),
            RecipeTypePicker(TypeOfRecipe.SIDE_DISH, "Side Dish"),
            RecipeTypePicker(TypeOfRecipe.DESSERT, "Dessert"),
            RecipeTypePicker(TypeOfRecipe.DRINK, "Drink"),
            RecipeTypePicker(TypeOfRecipe.SAUCE, "Sauce")
        ),
        optionItemFormatter = { "${it?.name}" },
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )
    @FormField
    val numberOfPeople = FieldState(
        state = mutableStateOf(null),
        options = mutableListOf(
            NumberPicker(1),
            NumberPicker(2),
            NumberPicker(3),
            NumberPicker(4),
            NumberPicker(5)
        ),
        optionItemFormatter = { it?.value.toString() },
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )
//    var preparationTime: Duration, //probably string ? see after
//    var cookingTime: Duration, //probably string ? see after
//    var restingTime: Duration, //probably string ? see after
    @FormField
    val cookingType = FieldState(
        state = mutableStateOf(null),
        options = mutableListOf(
            CookingTypePicker(CookingType.NO_COOKING, "No Cooking"),
            CookingTypePicker(CookingType.STOVE, "Stove"),
            CookingTypePicker(CookingType.OVEN, "Oven"),
            CookingTypePicker(CookingType.MICROWAVE, "Microwave"),
            CookingTypePicker(CookingType.OTHER, "Other")
        ),
        optionItemFormatter = { "${it?.name}" },
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )
//    var posterPath: String,
    val ingredients = ListFieldState(
        state = mutableStateListOf<IngredientItem>(),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )

    val steps = ListFieldState(
        state = mutableStateListOf<StepItem>(),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )
//    var moreInformation: String,

}