package com.example.worldchangingcookingapp.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ch.benlu.composeform.*
import ch.benlu.composeform.components.TextFieldComponent
import ch.benlu.composeform.fields.PickerValue
import ch.benlu.composeform.validators.MinLengthValidator
import ch.benlu.composeform.validators.NotEmptyValidator
import com.example.worldchangingcookingapp.form.fields.LargeTextPopup
import com.example.worldchangingcookingapp.form.fields.ListItem
import com.example.worldchangingcookingapp.form.fields.ListItemFactory
import com.example.worldchangingcookingapp.models.CookingType
import com.example.worldchangingcookingapp.models.CookingTypePicker
import com.example.worldchangingcookingapp.models.Difficulty
import com.example.worldchangingcookingapp.models.DifficultyPicker
import com.example.worldchangingcookingapp.models.IngredientItem
import com.example.worldchangingcookingapp.models.Price
import com.example.worldchangingcookingapp.models.PricePicker
import com.example.worldchangingcookingapp.models.RecipeTypePicker
import com.example.worldchangingcookingapp.models.TypeOfRecipe
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

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

        var isDialogVisible by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { isDialogVisible = it.isFocused },
            value = state.value,
            readOnly = true,
            onValueChange = {}
        )


        if (isDialogVisible) {
            LargeTextPopup(
                initialValue = state.value,
                callBack = {
                    state.value = it
                    isDialogVisible = false
                    focusManager.clearFocus()
                },
                title = "Create Step"
            )
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
    val description = FieldState(
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

    @FormField
    var preparationTime = FieldState(
        state = mutableStateOf<Duration?>(null),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )

    @FormField
    var cookingTime = FieldState(
        state = mutableStateOf<Duration?>(null),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )

    @FormField
    var restingTime = FieldState(
        state = mutableStateOf<Duration?>(null),
        validators = mutableListOf(
            NotEmptyValidator()
        )
    )
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

    @FormField
    val moreInformation = FieldState(
        state = mutableStateOf(null),
        validators = mutableListOf(
            NotEmptyValidator(),
            MinLengthValidator(
                minLength = 3,
                errorText = "Must be 3 characters long!"
            )
        )
    )
}