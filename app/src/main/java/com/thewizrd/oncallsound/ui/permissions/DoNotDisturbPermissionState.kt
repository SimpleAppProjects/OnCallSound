package com.thewizrd.oncallsound.ui.permissions

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.thewizrd.oncallsound.utils.isNotificationAccessAllowed

/**
 * Based on Google Accompanist Permissions module
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberDoNotDisturbAccessState(
    onPermissionResult: (Boolean) -> Unit = {}
): PermissionState {
    val context = LocalContext.current
    val permissionState = remember {
        DoNotDisturbPermissionState(context, context.findActivity())
    }

    // Refresh the permission status when the lifecycle is resumed
    PermissionLifecycleCheckerEffect(permissionState)

    // Register receiver
    SystemBroadcastReceiver(NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED) {
        permissionState.refreshPermissionStatus()
        onPermissionResult(permissionState.status.isGranted)
    }

    return permissionState
}

@OptIn(ExperimentalPermissionsApi::class)
internal class DoNotDisturbPermissionState(
    private val context: Context,
    private val activity: Activity
) : PermissionState {
    override val permission: String
        get() = Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS

    override var status: PermissionStatus by mutableStateOf(getPermissionStatus())

    override fun launchPermissionRequest() {
        runCatching {
            activity.startActivity(Intent(permission))
        }
    }

    internal fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    private fun getPermissionStatus(): PermissionStatus {
        val hasPermission = context.isNotificationAccessAllowed()
        return if (hasPermission) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied(false)
        }
    }
}

@ExperimentalPermissionsApi
@Composable
internal fun PermissionLifecycleCheckerEffect(
    permissionState: DoNotDisturbPermissionState,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME
) {
    // Check if the permission was granted when the lifecycle is resumed.
    // The user might've gone to the Settings screen and granted the permission.
    val permissionCheckerObserver = remember(permissionState) {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                // If the permission is revoked, check again.
                // We don't check if the permission was denied as that triggers a process restart.
                if (permissionState.status != PermissionStatus.Granted) {
                    permissionState.refreshPermissionStatus()
                }
            }
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionCheckerObserver) {
        lifecycle.addObserver(permissionCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionCheckerObserver) }
    }
}

/**
 * Find the closest Activity in a given Context.
 */
internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}