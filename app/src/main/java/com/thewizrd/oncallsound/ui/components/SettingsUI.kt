package com.thewizrd.oncallsound.ui.components

import android.Manifest
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.thewizrd.oncallsound.preferences.RingerMode
import com.thewizrd.oncallsound.ui.components.preferences.ListPreference
import com.thewizrd.oncallsound.ui.components.preferences.PreferenceCategory
import com.thewizrd.oncallsound.ui.components.preferences.SwitchPreference
import com.thewizrd.oncallsound.ui.permissions.rememberDoNotDisturbAccessState
import com.thewizrd.oncallsound.ui.theme.AppTheme
import com.thewizrd.oncallsound.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsUI(
    settingsViewModel: SettingsViewModel
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    val scrollState = rememberScrollState()

    val preferences = settingsViewModel.preferences

    val openPermissionsDialog = remember { mutableStateOf(false) }

    val phoneStatePermissionState = rememberPermissionState(
        Manifest.permission.READ_PHONE_STATE
    )
    val doNotDisturbAccessState = rememberDoNotDisturbAccessState()

    if (openPermissionsDialog.value) {
        PermissionsAlertDialog(
            phoneStatePermissionState,
            doNotDisturbAccessState
        ) {
            openPermissionsDialog.value = false
        }
    }

    AppTheme {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(text = "Settings")
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                if (!phoneStatePermissionState.status.isGranted || !doNotDisturbAccessState.status.isGranted) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = CardDefaults.elevatedCardColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(
                                    horizontal = 8.dp
                                ),
                        ) {
                            Text(
                                text = "Missing permissions",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 16.dp,
                                        horizontal = 8.dp
                                    ),
                                style = MaterialTheme.typography.titleMedium,
                            )
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
                }
                PreferenceCategory(title = "General")
                SwitchPreference(
                    title = "Enable Service",
                    subtitle = "Enable call state listener service",
                    checked = preferences.enableService,
                    onCheckedChange = { enable ->
                        if (enable) {
                            if (!phoneStatePermissionState.status.isGranted || !doNotDisturbAccessState.status.isGranted) {
                                openPermissionsDialog.value = true
                            } else {
                                settingsViewModel.enableService(enable)
                            }
                        } else {
                            settingsViewModel.enableService(enable)
                        }
                    },
                    enabled = phoneStatePermissionState.status.isGranted && doNotDisturbAccessState.status.isGranted
                )
                ListPreference(
                    title = "Mode on Call End",
                    items = RingerMode.values().map {
                        when (it) {
                            RingerMode.NORMAL -> (it to "Sound")
                            RingerMode.VIBRATE -> (it to "Vibrate")
                            RingerMode.SILENT -> (it to "Silent")
                        }
                    },
                    selectedItem = preferences.ringerModeOnCallEnd,
                    onItemSelected = { mode ->
                        settingsViewModel.setRingerModeOnCallEnd(mode)
                    }
                )
            }
        }
    }
}