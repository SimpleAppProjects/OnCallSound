package com.thewizrd.oncallsound

import android.app.Application
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        appDeps = object : AppDependencies() {
            override val application: Application = this@App
        }

        appDeps.appScope.launch {
            appDeps.callMonitorService.init()
        }
    }

    override fun onTerminate() {
        appDeps.appScope.cancel()
        super.onTerminate()
    }
}