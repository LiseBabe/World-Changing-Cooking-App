package com.example.worldchangingcookingapp.form.fields

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import ch.benlu.composeform.Field
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form


//Modified version original from ch.benlu
class TextField(
    label: String,
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
    @SuppressLint("NotConstructor")
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if (!fieldState.isVisible()) {
            return
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = formatter?.invoke(value.value) ?: (value.value ?: ""),
            onValueChange = {
                onChange(it, form)
            },
            keyboardOptions = KeyboardOptions(imeAction = imeAction ?: ImeAction.Next, keyboardType = keyboardType),
            keyboardActions = KeyboardActions.Default,
            enabled = isEnabled,
            isError = fieldState.hasError(),
            label = {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = label
                )
            },
            visualTransformation = visualTransformation,
            placeholder = null
        )
    }
}