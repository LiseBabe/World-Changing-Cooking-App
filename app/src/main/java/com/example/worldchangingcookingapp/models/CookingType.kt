package com.example.worldchangingcookingapp.models

import ch.benlu.composeform.fields.PickerValue

enum class CookingType {
    OVEN, STOVE, MICROWAVE, NO_COOKING, OTHER
}

data class CookingTypePicker (
    val type: CookingType,
    val name: String
): PickerValue() {
    override fun searchFilter(query: String): Boolean {
        return this.name.startsWith(query)
    }
}