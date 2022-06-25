package com.thewizrd.oncallsound.services

import android.os.Build
import android.telephony.TelephonyCallback
import androidx.annotation.RequiresApi
import com.thewizrd.oncallsound.appDeps

@RequiresApi(Build.VERSION_CODES.S)
class CallStateCallback : TelephonyCallback(), TelephonyCallback.CallStateListener {
    override fun onCallStateChanged(state: Int) {
        appDeps.callStateHandler.onCallStateChanged(state)
    }
}