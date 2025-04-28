package com.example.worldchangingcookingapp.form.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ch.benlu.composeform.*
import com.example.worldchangingcookingapp.form.ListFieldState
import com.example.worldchangingcookingapp.ui.theme.WorldChangingCookingAppTheme

interface ListItem<T> {
    var state : MutableState<T>
    @Composable
    fun ElementBuilder(modifier : Modifier)
}

interface ListItemFactory<T> {
    fun newListItem(): ListItem<T>
}

class ListField<T, E : ListItem<T>>(
    val label: String,
    val form: Form,
    val modifier: Modifier? = Modifier,
    val fieldState: ListFieldState<E>,
    val isEnabled: Boolean = true,
    val imeAction: ImeAction = ImeAction.Next,
    val formatter: ((raw: List<E>?) -> String)? = null,
    private val itemFactory: ListItemFactory<T>,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    var changed: ((v: E?) -> Unit)? = null
) {
    var value: MutableList<E?> = mutableStateListOf()

    fun add(v: E, form: Form) {
        println("Adding new item")
        this.value.add(v)
        this.fieldState.state.add(v)
        form.validate()
        this.fieldState.hasChanges.value = true
        changed?.invoke(v)
    }

    fun removeAt(i: Int, form: Form) {
        this.value.removeAt(i)
        val v = this.fieldState.state.removeAt(i)
        form.validate()
        this.fieldState.hasChanges.value = true
        changed?.invoke(v)
    }

    fun updateComposableValue() {
        this.value.clear()
        @Suppress("UNCHECKED_CAST")
        this.value.addAll(fieldState.state.toList())
    }

    @Composable
    fun Field() {
        // (1) update & check visibility
        this.updateComposableValue()
        if(!fieldState.isVisible()) return

        // (2) render your composable -----------------
        Column(modifier = modifier ?: Modifier) {
            Text(label)
            fieldState.state.forEachIndexed { index, listItem ->
                Row {
                    listItem.ElementBuilder(
                        modifier = modifier?.weight(1f)?.align(Alignment.CenterVertically)
                            ?: Modifier
                    )
                    IconButton(
                        onClick = {
                            removeAt(index, form)
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete List Item")
                    }
                }
            }

            IconButton(
                onClick = {
                    add(itemFactory.newListItem() as E, form)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Item")
            }

        }

        if (fieldState.hasError()) {
            Text(
                text = fieldState.errorText.joinToString("\n"),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = TextStyle.Default.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}