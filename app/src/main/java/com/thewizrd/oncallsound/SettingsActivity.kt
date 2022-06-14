package com.thewizrd.oncallsound

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.thewizrd.oncallsound.ui.components.SettingsUI
import com.thewizrd.oncallsound.ui.viewmodels.SettingsViewModel

class SettingsActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SettingsUI(settingsViewModel)
        }
    }

    override fun onStart() {
        super.onStart()
    }
}