package com.thewizrd.oncallsound

import android.app.Application
import com.thewizrd.oncallsound.preferences.UserPreferencesRepository
import com.thewizrd.oncallsound.preferences.dataStore
import com.thewizrd.oncallsound.services.CallMonitorService
import com.thewizrd.oncallsound.services.CallStateHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

public lateinit var appDeps: AppDependencies

public abstract class AppDependencies {
    public abstract val application: Application
    open val appScope: CoroutineScope = MainScope()
    val callStateHandler: CallStateHandler = CallStateHandler()

    public val userPreferencesRepo: UserPreferencesRepository by lazy {
        UserPreferencesRepository(application.dataStore)
    }

    public val callMonitorService: CallMonitorService by lazy {
        CallMonitorService(application.applicationContext)
    }
}