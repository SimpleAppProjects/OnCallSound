package com.thewizrd.oncallsound

import android.app.Application
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class App : Application() {
    private val applicationScope = MainScope()

    override fun onCreate() {
        super.onCreate()

        appDeps = object : AppDependencies() {
            override val application: Application = this@App
        }

        applicationScope.launch {
            appDeps.callMonitorService.init()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}