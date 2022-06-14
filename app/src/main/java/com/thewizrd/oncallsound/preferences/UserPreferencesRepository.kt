package com.thewizrd.oncallsound.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class RingerMode {
    NORMAL,
    VIBRATE,
    SILENT
}

data class UserPreferences(
    val enableService: Boolean = false,
    val ringerModeOnCallEnd: RingerMode = RingerMode.VIBRATE
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    companion object {
        private const val TAG: String = "UserPreferencesRepo"
    }

    private object PreferenceKeys {
        val ENABLE_CALLSERVICE = booleanPreferencesKey("key_enable_callservice")
        val MODE_ON_CALLEND = stringPreferencesKey("key_mode_on_callend")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            mapUserPreferences(preference)
        }

    val serviceEnabled: Flow<Boolean> = userPreferencesFlow.map {
        it.enableService
    }

    suspend fun setServiceEnabled(enable: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.ENABLE_CALLSERVICE] = enable
        }
    }

    val ringerModeOnCallEnd: Flow<RingerMode> = userPreferencesFlow.map {
        it.ringerModeOnCallEnd
    }

    suspend fun setRingerModeOnCallEnd(mode: RingerMode) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.MODE_ON_CALLEND] = mode.name
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        return UserPreferences(
            enableService = preferences[PreferenceKeys.ENABLE_CALLSERVICE] ?: false,
            ringerModeOnCallEnd = RingerMode.valueOf(
                preferences[PreferenceKeys.MODE_ON_CALLEND] ?: RingerMode.VIBRATE.name
            )
        )
    }
}