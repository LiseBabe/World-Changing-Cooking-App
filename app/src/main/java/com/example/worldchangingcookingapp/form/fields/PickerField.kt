package com.example.worldchangingcookingapp.form.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ch.benlu.composeform.Field
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import ch.benlu.composeform.fields.PickerValue

//Modified version original from ch.benlu
@Composable
fun <T>RadioButtonComponent(
    label: String,
    value: T? = null,
    selectedValue: T?,
    onClickListener: (T?) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = value == selectedValue,
                onClick = {
                    onClickListener(value)
                }
            )
    ) {
        RadioButton(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(alignment = Alignment.CenterVertically),
            selected = value == selectedValue,
            onClick = {
                onClickListener(value)
            }
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.CenterVertically)
        )
    }
}

//Modified version original from ch.benlus
@Composable
fun <T> SingleSelectDialogComponent(
    title: String,
    text: String? = null,
    optionsList: MutableList<T?>,
    defaultSelected: T?,
    submitButtonText: String,
    onSubmitButtonClick: (T?) -> Unit,
    onDismissRequest: () -> Unit,
    optionItemFormatter: ((T?) -> String)? = null,
) {

    val selectedOption =
        remember { mutableIntStateOf(optionsList.indexOfFirst { it == defaultSelected }) }
    val query = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Surface (
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (text != null) {
                        Text(text = text)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                LazyColumn(
                    modifier = Modifier.wrapContentHeight()
                ) {
                    item {
                    }
                    items(
                        items = optionsList,
                        key = { i ->
                            i.toString()
                        }
                    ) { item ->
                        RadioButtonComponent(
                            label = optionItemFormatter?.invoke(item) ?: item.toString(),
                            value = item,
                            selectedValue = optionsList.getOrNull(selectedOption.intValue),
                        ) { selectedValue ->
                            selectedOption.intValue =
                                optionsList.indexOfFirst { o -> o == selectedValue }
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onDismissRequest.invoke()
                            },
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(
                            modifier = Modifier.width(16.dp)
                        )
                        Button(
                            onClick = {
                                if (selectedOption.intValue >= 0 && optionsList.size > selectedOption.intValue) {
                                    onSubmitButtonClick.invoke(optionsList[selectedOption.intValue])
                                }
                                onDismissRequest.invoke()
                            },
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(text = submitButtonText)
                        }
                    }
                }
            }
        }
    }
}

//Modified version original from ch.benlu
class PickerField<T: PickerValue>(
    label: String,
    form: Form,
    modifier: Modifier? = Modifier,
    fieldState: FieldState<T?>,
    isEnabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    formatter: ((raw: T?) -> String)? = null,
    private val isSearchable: Boolean = true,
    changed: ((v: T?) -> Unit)? = null
) : Field<T>(
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
        if (!fieldState.isVisible()) {
            return
        }

        var isDialogVisible by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isDialogVisible = it.isFocused
                },
            label = {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = label
                )
            },
            value = fieldState.selectedOptionText() ?: "",
            onValueChange = {},
            isError = fieldState.hasError(),
            readOnly = true,
            trailingIcon = {
                Icon(
                    if (isDialogVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    null
                )
            }
        )

        if (isDialogVisible) {
            SingleSelectDialogComponent(
                title = label,
                optionsList = fieldState.options!!,
                optionItemFormatter = fieldState.optionItemFormatter,
                defaultSelected = fieldState.state.value,
                submitButtonText = stringResource(id = android.R.string.ok),
                onSubmitButtonClick = {
                    isDialogVisible = false
                    this.onChange(it, form)
                    focusManager.clearFocus()
                },
                onDismissRequest = {
                    isDialogVisible = false
                    focusManager.clearFocus()
                }
            )
        }
    }

}