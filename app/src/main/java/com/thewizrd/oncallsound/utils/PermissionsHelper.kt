package com.thewizrd.oncallsound.utils

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import androidx.core.content.ContextCompat
import com.thewizrd.oncallsound.preferences.RingerMode

fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

fun Context.callStatePermissionEnabled(): Boolean {
    return checkPermission(Manifest.permission.READ_PHONE_STATE)
}

fun Context.isNotificationAccessAllowed(): Boolean {
    val notifMgr = getSystemService(NotificationManager::class.java)
    return notifMgr.isNotificationPolicyAccessGranted
}

fun Context.getRingerState(): RingerMode {
    val audioMgr = getSystemService(AudioManager::class.java)

    return when (audioMgr.ringerMode) {
        AudioManager.RINGER_MODE_NORMAL -> RingerMode.NORMAL
        AudioManager.RINGER_MODE_VIBRATE -> RingerMode.VIBRATE
        AudioManager.RINGER_MODE_SILENT -> RingerMode.SILENT
        else -> RingerMode.NORMAL
    }
}

fun Context.setRingerState(mode: RingerMode) {
    val audioMgr = getSystemService(AudioManager::class.java)

    when (mode) {
        RingerMode.NORMAL -> {
            audioMgr.ringerMode = AudioManager.RINGER_MODE_NORMAL
        }
        RingerMode.VIBRATE -> {
            audioMgr.ringerMode = AudioManager.RINGER_MODE_VIBRATE
        }
        RingerMode.SILENT -> {
            val dndState = getDNDState()

            audioMgr.ringerMode = AudioManager.RINGER_MODE_SILENT

            /*
             * Setting ringerMode to silent may trigger Do Not Disturb mode to change
             * In case this happens, set it back to its original state
             */
            if (dndState != getDNDState()) {
                setDNDState(dndState)
            }
        }
    }
}

private fun Context.getDNDState(): Int {
    val notifMgr = getSystemService(NotificationManager::class.java)
    return notifMgr.currentInterruptionFilter
}

private fun Context.setDNDState(filterMode: Int) {
    val notifMgr = getSystemService(NotificationManager::class.java)
    notifMgr.setInterruptionFilter(filterMode)
}