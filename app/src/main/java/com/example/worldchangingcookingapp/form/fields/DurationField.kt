package com.example.worldchangingcookingapp.form.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ch.benlu.composeform.Field
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import ch.benlu.composeform.components.TextFieldComponent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DurationField(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<Duration?>,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: Duration?) -> String)? = null,
    changed: ((v: Duration?) -> Unit)? = null,
) : Field<Duration?>(
    label = label,
    form = form,
    fieldState = fieldState,
    isEnabled = isEnabled,
    modifier = modifier,
    imeAction = imeAction,
    formatter = formatter,
    changed = changed
) {
    @Composable
    override fun Field() {
        this.updateComposableValue()
        if(!fieldState.isVisible()) return

        var isDialogVisible by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        TextFieldComponent(
            modifier = modifier ?: Modifier,
            imeAction = imeAction ?: ImeAction.Next,
            isEnabled = isEnabled,
            label = label,
            text = formatter?.invoke(value.value) ?: (value.value?.toString() ?: ""),
            hasError = fieldState.hasError(),
            errorText = fieldState.errorText,
            isReadOnly = true,
            visualTransformation = visualTransformation,
            focusRequester = focusRequester,
            focusChanged = {
                isDialogVisible = it.isFocused
            }
        )

        if (isDialogVisible) {

            var finalTime : Duration? = value.value
            var th : String? = null
            var tm : String? = null
            var ts : String? = null
            finalTime?.toComponents { h, m, s, _ -> {
                th = if (h.toInt() == 0) null else "$h"
                ts = if (s.toInt() == 0) null else "$s"
                tm = if (m.toInt() == 0) null else "$m"
            }() }
            var seconds = remember { mutableStateOf(ts ?: "") }
            var minutes = remember { mutableStateOf(tm ?: "") }
            var hours = remember { mutableStateOf(th ?: "") }

            Dialog(onDismissRequest = {
                isDialogVisible = false
                focusManager.clearFocus()
                if (hours.value == "") hours.value = "0"
                if (minutes.value == "") minutes.value = "0"
                if (seconds.value == "") seconds.value = "0"
                finalTime = try {
                    (hours.value.toInt() * 3600 +
                            minutes.value.toInt() * 60 +
                            seconds.value.toInt()).seconds
                } catch (e : NumberFormatException) {
                    0.seconds
                }
                onChange(finalTime, form)
            }) {
                Surface (
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                        Text(text = "Select Duration",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 8.dp))

                        Row (verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.Center,
                            modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                modifier = Modifier.weight(1f),
                                value = hours.value,
                                label = { Text(text = "Hours") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                onValueChange = {
                                    hours.value = it
                                }
                            )
                            Text(":", style = MaterialTheme.typography.headlineSmall)
                            TextField(
                                modifier = Modifier.weight(1f),
                                value = minutes.value,
                                label = { Text(text = "Min") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                onValueChange = {
                                    minutes.value = it
                                }
                            )
                            Text(":", style = MaterialTheme.typography.headlineSmall)
                            TextField(
                                modifier = Modifier.weight(1f),
                                value = seconds.value,
                                label = { Text(text = "Sec") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                onValueChange = {
                                    seconds.value = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}