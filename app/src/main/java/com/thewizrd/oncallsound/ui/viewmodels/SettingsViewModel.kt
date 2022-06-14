package com.thewizrd.oncallsound.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewizrd.oncallsound.appDeps
import com.thewizrd.oncallsound.preferences.RingerMode
import com.thewizrd.oncallsound.preferences.UserPreferences
import com.thewizrd.oncallsound.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val userPrefsRepo: UserPreferencesRepository = appDeps.userPreferencesRepo

    // Keep the user preferences as a stream of changes
    private val userPreferencesFlow = userPrefsRepo.userPreferencesFlow

    var preferences by mutableStateOf(UserPreferences())
        private set

    init {
        userPreferencesFlow.onEach {
            preferences = it
        }.launchIn(viewModelScope)
    }

    fun enableService(enable: Boolean) {
        viewModelScope.launch {
            userPrefsRepo.setServiceEnabled(enable)

            if (enable) {
                appDeps.callMonitorService.registerListener()
            } else {
                appDeps.callMonitorService.unregisterListener()
            }
        }
    }

    fun setRingerModeOnCallEnd(mode: RingerMode) {
        viewModelScope.launch {
            userPrefsRepo.setRingerModeOnCallEnd(mode)
        }
    }
}