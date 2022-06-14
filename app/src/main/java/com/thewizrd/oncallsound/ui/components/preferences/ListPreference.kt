package com.thewizrd.oncallsound.ui.components.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ListPreference(
    title: String,
    items: List<Pair<T, String>>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val openDialog = remember { mutableStateOf(false) }

    Preference(
        title = title,
        subtitle = items.first { it.first == selectedItem }.second,
        onClick = {
            openDialog.value = true
        },
        modifier = modifier,
        enabled = enabled,
    )

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            confirmButton = {
                /*
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
                 */
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            },
            text = {
                Column(Modifier.selectableGroup()) {
                    items.forEach { item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .selectable(
                                    selected = (selectedItem == item.first),
                                    onClick = {
                                        onItemSelected(item.first)
                                        openDialog.value = false
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = (selectedItem == item.first),
                                onClick = null, // null recommended for accessibility with screenreaders
                            )
                            Text(
                                text = item.second,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListPreferencePreview() {
    val options = listOf(
        "1" to "One",
        "2" to "Two",
        "3" to "Three",
    )
    var selectedTheme by remember { mutableStateOf(value = options.first()) }

    ListPreference(
        title = "List Preference Title",
        items = options,
        selectedItem = selectedTheme,
        onItemSelected = { selected ->
            selectedTheme = options.first { it.first == selected }
        },
    )
}