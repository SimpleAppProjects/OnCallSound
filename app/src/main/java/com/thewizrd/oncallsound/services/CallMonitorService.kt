package com.thewizrd.oncallsound.services

import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.thewizrd.oncallsound.appDeps
import com.thewizrd.oncallsound.utils.callStatePermissionEnabled
import com.thewizrd.oncallsound.utils.isNotificationAccessAllowed
import java.util.concurrent.Executors

@Suppress("DEPRECATION")
class CallMonitorService(context: Context) {
    private val appContext = context.applicationContext

    private val mTelephonyManager: TelephonyManager =
        appContext.getSystemService(TelephonyManager::class.java)
    private var callStateCallback: CallStateCallback? = null
    private var callStateListener: CallStateListener? = null

    private var listenerRegistered: Boolean = false

    val isActive: Boolean
        get() = listenerRegistered

    internal suspend fun init() {
        if (appContext.callStatePermissionEnabled() &&
            appContext.isNotificationAccessAllowed()
        ) {
            registerListener()
        } else {
            unregisterListener()
            appDeps.userPreferencesRepo.setServiceEnabled(false)
        }
    }

    fun registerListener() {
        if (listenerRegistered) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val executor = Executors.newSingleThreadExecutor()
            mTelephonyManager.registerTelephonyCallback(
                executor,
                CallStateCallback(executor).also { callStateCallback = it }
            )
        } else {
            val phoneStateListener = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                CallStateListener(Executors.newSingleThreadExecutor())
            } else {
                CallStateListener()
            }.also { callStateListener = it }

            mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        }

        listenerRegistered = true
    }

    fun unregisterListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            callStateCallback?.run {
                mTelephonyManager.unregisterTelephonyCallback(this)
            }
            callStateCallback = null
        } else {
            callStateListener?.run {
                mTelephonyManager.listen(this, PhoneStateListener.LISTEN_NONE)
            }
            callStateListener = null
        }
    }
}