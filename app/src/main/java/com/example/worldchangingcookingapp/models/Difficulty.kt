package com.example.worldchangingcookingapp.models

import ch.benlu.composeform.fields.PickerValue

enum class Difficulty {
    REALLY_EASY, EASY, MEDIUM, HARD, REALLY_HARD
}

data class DifficultyPicker(
    val type : Difficulty,
    val name : String
) : PickerValue() {
    override fun searchFilter(query: String): Boolean {
        return this.name.startsWith(query)
    }
}