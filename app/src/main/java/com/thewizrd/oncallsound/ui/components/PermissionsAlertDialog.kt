package com.thewizrd.oncallsound.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsAlertDialog(
    phoneStatePermissionState: PermissionState,
    doNotDisturbAccessState: PermissionState,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Permissions",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
        text = {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable(
                            enabled = !phoneStatePermissionState.status.isGranted
                        ) {
                            phoneStatePermissionState.launchPermissionRequest()
                        }
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Phone State",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 0.dp)
                    )
                    if (phoneStatePermissionState.status.isGranted) {
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = "Permission granted",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            tint = Color.Green
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Clear,
                            contentDescription = "Permission denied",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            tint = Color.Red
                        )
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable(
                            enabled = !doNotDisturbAccessState.status.isGranted
                        ) {
                            doNotDisturbAccessState.launchPermissionRequest()
                        }
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Do Not Disturb Access",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 0.dp)
                    )
                    if (doNotDisturbAccessState.status.isGranted) {
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = "Permission granted",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            tint = Color.Green
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Clear,
                            contentDescription = "Permission denied",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    )
}

@Preview
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionsAlertDialogPreview() {
    val phoneState = object : PermissionState {
        override val permission: String
            get() = ""
        override val status: PermissionStatus
            get() = PermissionStatus.Granted

        override fun launchPermissionRequest() {
        }
    }
    val dndAccessState = object : PermissionState {
        override val permission: String
            get() = ""
        override val status: PermissionStatus
            get() = PermissionStatus.Denied(false)

        override fun launchPermissionRequest() {
        }
    }

    PermissionsAlertDialog(
        phoneStatePermissionState = phoneState,
        doNotDisturbAccessState = dndAccessState
    ) {
    }
}