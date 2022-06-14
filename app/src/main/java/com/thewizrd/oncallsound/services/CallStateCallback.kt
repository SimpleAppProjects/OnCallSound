package com.thewizrd.oncallsound.services

import android.os.Build
import android.telephony.TelephonyCallback
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

@RequiresApi(Build.VERSION_CODES.S)
class CallStateCallback(executor: Executor) : TelephonyCallback(),
    TelephonyCallback.CallStateListener {
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + executor.asCoroutineDispatcher())
    private var wasInCall = false

    override fun onCallStateChanged(state: Int) {
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