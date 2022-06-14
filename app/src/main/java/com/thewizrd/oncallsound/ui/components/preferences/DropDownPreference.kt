package com.thewizrd.oncallsound.ui.components.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun <T> DropDownPreference(
    title: String,
    items: List<Pair<T, String>>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var dropDownExpanded by remember { mutableStateOf(value = false) }

    Preference(
        title = title,
        subtitle = items.first { it.first == selectedItem }.second,
        onClick = {
            dropDownExpanded = true
        },
        modifier = modifier
            .background(
                color = if (dropDownExpanded) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                } else {
                    Color.Unspecified
                }
            ),
        enabled = enabled,
    )

    Box {
        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { dropDownExpanded = !dropDownExpanded },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        dropDownExpanded = false

                        onItemSelected(item.first)
                    },
                    modifier = Modifier
                        .background(
                            color = if (selectedItem == item.first) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            } else {
                                Color.Unspecified
                            },
                        ),
                    text = {
                        Text(
                            text = item.second,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DropDownPreferencePreview() {
    val themes = listOf(
        "system" to "System default",
        "light" to "Light",
        "dark" to "Dark",
    )
    var selectedTheme by remember { mutableStateOf(value = themes.first()) }

    DropDownPreference(
        title = "Theme",
        items = themes,
        selectedItem = selectedTheme,
        onItemSelected = { selected ->
            selectedTheme = themes.first { it.first == selected }
        },
    )
}