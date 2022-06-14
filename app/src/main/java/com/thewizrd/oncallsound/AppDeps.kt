package com.thewizrd.oncallsound

import android.app.Application
import com.thewizrd.oncallsound.preferences.UserPreferencesRepository
import com.thewizrd.oncallsound.preferences.dataStore
import com.thewizrd.oncallsound.services.CallMonitorService

public lateinit var appDeps: AppDependencies

public abstract class AppDependencies {
    public abstract val application: Application

    public val userPreferencesRepo: UserPreferencesRepository by lazy {
        UserPreferencesRepository(application.dataStore)
    }

    public val callMonitorService: CallMonitorService by lazy {
        CallMonitorService(application.applicationContext)
    }
}