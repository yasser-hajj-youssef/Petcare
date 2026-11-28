package com.example.petcare.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(
    name = "settings"
)


enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}


class SettingsRepository(private val context: Context) {

    private val remindersKey =
        booleanPreferencesKey("reminders_enabled")

    private val themeKey =
        stringPreferencesKey("theme_mode")


    val remindersEnabled: Flow<Boolean> =
        context.dataStore.data.map { prefs ->

            prefs[remindersKey] ?: false
        }


    val themeMode: Flow<ThemeMode> =
        context.dataStore.data.map { prefs ->

            ThemeMode.entries.firstOrNull {
                it.name == prefs[themeKey]
            } ?: ThemeMode.SYSTEM
        }


    suspend fun setRemindersEnabled(enabled: Boolean) {

        context.dataStore.edit { prefs ->

            prefs[remindersKey] = enabled
        }
    }


    suspend fun setThemeMode(mode: ThemeMode) {

        context.dataStore.edit { prefs ->

            prefs[themeKey] = mode.name
        }
    }
}