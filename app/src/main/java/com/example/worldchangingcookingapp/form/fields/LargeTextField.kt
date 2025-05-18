package com.example.worldchangingcookingapp.form.fields

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import ch.benlu.composeform.Field
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form

class LargeTextField(
    label: String,
    val popUpTitle: String = "",
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<String?>,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: String?) -> String)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    changed: ((v: String?) -> Unit)? = null
) : Field<String>(
    label = label,
    form = form,
    fieldState = fieldState,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter,
    keyboardType = keyboardType,
    visualTransformation = visualTransformation,
    changed = changed
) {

    /**
     * Returns a composable representing the DateField / Picker for this field
     */
    @SuppressLint("NotConstructor")
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!fieldState.isVisible()) {
            return
        }

        var isDialogVisible by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            label = { Text(label) },
            modifier = (modifier?:Modifier)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isDialogVisible = it.isFocused
                },
            value = value.value ?: "",
            readOnly = true,
            onValueChange = {},
        )

        if (isDialogVisible) {
            LargeTextPopup(
                initialValue = value.value ?: "",
                callBack = {
                    onChange(it, form)
                    isDialogVisible = false
                    focusManager.clearFocus()
                },
                title = popUpTitle
            )
        }
    }
}
