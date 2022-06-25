package com.thewizrd.oncallsound.services

import android.os.Build
import android.telephony.PhoneStateListener
import androidx.annotation.RequiresApi
import com.thewizrd.oncallsound.appDeps
import java.util.concurrent.Executor

@Suppress("DEPRECATION")
class CallStateListener : PhoneStateListener {
    constructor()

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(executor: Executor) : super(executor)

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        appDeps.callStateHandler.onCallStateChanged(state)
    }
}