package com.example.worldchangingcookingapp.models

import ch.benlu.composeform.fields.PickerValue

enum class Price {
    CHEAP, MODERATE, EXPENSIVE
}

data class PricePicker(
    val type : Price,
    val name : String
) : PickerValue() {
    override fun searchFilter(query: String): Boolean {
        return this.name.startsWith(query)
    }
}