package com.example.worldchangingcookingapp.models

import ch.benlu.composeform.fields.PickerValue

enum class TypeOfRecipe {
    STARTER, MAIN_COURSE, DESSERT, SIDE_DISH, APPETIZER, DRINK, CONFECTIONERY, SAUCE
}

data class RecipeTypePicker(
    val type : TypeOfRecipe,
    val name : String
) : PickerValue() {
    override fun searchFilter(query: String): Boolean {
        return this.name.startsWith(query)
    }
}