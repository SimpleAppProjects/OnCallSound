package com.thewizrd.oncallsound.preferences

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.thewizrd.oncallsound.appDeps
import kotlinx.coroutines.launch

class PhoneStateBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED == intent?.action) {
            if (!appDeps.callMonitorService.isActive) {
                appDeps.appScope.launch {
                    appDeps.callMonitorService.init()

                    when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                        TelephonyManager.EXTRA_STATE_IDLE -> {
                            appDeps.callStateHandler.onCallStateChanged(TelephonyManager.CALL_STATE_IDLE)
                        }
                        TelephonyManager.EXTRA_STATE_RINGING -> {
                            appDeps.callStateHandler.onCallStateChanged(TelephonyManager.CALL_STATE_RINGING)
                        }
                        TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                            appDeps.callStateHandler.onCallStateChanged(TelephonyManager.CALL_STATE_OFFHOOK)
                        }
                    }
                }
            }
        }
    }
}