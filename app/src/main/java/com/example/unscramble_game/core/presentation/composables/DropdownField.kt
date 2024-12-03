package com.example.unscramble_game.core.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unscramble_game.core.presentation.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    placeholderText: String,
    items: ImmutableList<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedItem by rememberSaveable { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = !isExpanded
        },
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = selectedItem,
            onValueChange = {},
            placeholder = { Text(placeholderText) },
            trailingIcon = {
                val icon =
                    if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)

                        selectedItem = item
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DropdownFieldPreview() {
    AppTheme {
        DropdownField(
            placeholderText = "Placeholder",
            items = persistentListOf(),
            onItemSelected = {},
        )
    }
}
