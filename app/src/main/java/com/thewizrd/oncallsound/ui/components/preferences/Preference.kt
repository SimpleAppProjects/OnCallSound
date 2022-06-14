package com.thewizrd.oncallsound.ui.components.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Preference(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Preference(
        title = title,
        subtitle = AnnotatedString(text = subtitle),
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
fun Preference(
    title: String,
    subtitle: AnnotatedString,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick,
            )
            .padding(all = 16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (!enabled) {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            } else {
                Color.Unspecified
            },
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = if (!enabled) {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferencePreview() {
    Preference(
        title = "Preference Title",
        subtitle = AnnotatedString(text = "Summary for the preference"),
        onClick = { },
    )
}