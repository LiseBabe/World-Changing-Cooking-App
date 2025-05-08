package com.example.worldchangingcookingapp.form
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ch.benlu.composeform.Validator

class ListFieldState<T>(
    val state: MutableList<T>,
    val validators: MutableList<Validator<T>> = mutableListOf(),
    val errorText: MutableList<String> = mutableListOf(),
    val isValid: MutableState<Boolean?> = mutableStateOf(false),
    val isVisible: () -> Boolean = { true },
    val hasChanges: MutableState<Boolean?> = mutableStateOf(false),
    var options: MutableList<T> = mutableListOf(),
    val optionItemFormatter: ((T?) -> String)? = null,
) {
    fun hasError(): Boolean {
        return isVisible() && isValid.value == false && hasChanges.value == true
    }

    fun selectedOptions(): List<T>? {
        return options.toList()
    }
}