package com.thewizrd.oncallsound.services

import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import com.thewizrd.oncallsound.appDeps
import com.thewizrd.oncallsound.preferences.RingerMode
import com.thewizrd.oncallsound.utils.getRingerState
import com.thewizrd.oncallsound.utils.setRingerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Suppress("DEPRECATION")
class CallStateListener : PhoneStateListener {
    constructor() {
        scope = CoroutineScope(
            SupervisorJob() + Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(executor: Executor) : super(executor) {
        scope = CoroutineScope(SupervisorJob() + executor.asCoroutineDispatcher())
    }

    private val scope: CoroutineScope

    private var wasInCall = false

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                // No call active
                if (wasInCall) {
                    scope.launch {
                        val currentRingerMode = appDeps.application.getRingerState()
                        val newRingerMode = appDeps.userPreferencesRepo.ringerModeOnCallEnd.first()

                        if (currentRingerMode != newRingerMode) {
                            appDeps.application.setRingerState(newRingerMode)
                        }
                    }
                    wasInCall = false
                }
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                // Call is active; no other calls or ringing or waiting
                if (appDeps.application.getRingerState() != RingerMode.NORMAL) {
                    appDeps.application.setRingerState(RingerMode.NORMAL)
                }
                wasInCall = true
            }
            TelephonyManager.CALL_STATE_RINGING -> {
                // A new call is ringing or one call is active and new call is waiting
            }
        }
    }
}