package com.ehan.lashintbot.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import com.ehan.lashintbot.ui.theme.ThemeMode

data class UserPreferences(
    val username: String = "Pengguna Android",
    val isDarkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val counter: Int = 0,
    val accentColor: String = "Indigo",
    val themeMode: String = ThemeMode.SYSTEM.label
)

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val COUNTER = intPreferencesKey("counter")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val THEMEMODE = stringPreferencesKey("theme_mode")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val username: String = preferences[PreferencesKeys.USERNAME] ?: "Pengguna Android"
            val isDarkTheme: Boolean = preferences[PreferencesKeys.DARK_THEME] ?: false
            val notificationsEnabled: Boolean = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
            val counter: Int = preferences[PreferencesKeys.COUNTER] ?: 0
            val accentColor: String = preferences[PreferencesKeys.ACCENT_COLOR] ?: "Indigo"
            val themeMode: String = preferences[PreferencesKeys.THEMEMODE] ?: (ThemeMode.SYSTEM).label
            UserPreferences(
                username = username,
                isDarkTheme = isDarkTheme,
                notificationsEnabled = notificationsEnabled,
                counter = counter,
                accentColor = accentColor,
                themeMode = themeMode
            )
        }

    suspend fun updateUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
        }
    }

    suspend fun setThemeMode(_theme: String) {
        var theme: String = when (_theme) {
            ThemeMode.LIGHT.label -> {
                ThemeMode.LIGHT.label
            }
            ThemeMode.DARK.label -> {
                ThemeMode.DARK.label
            }
            else -> {
                ThemeMode.SYSTEM.label
            }
        }
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEMEMODE] = theme
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun incrementCounter() {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.COUNTER] ?: 0
            preferences[PreferencesKeys.COUNTER] = current + 1
        }
    }

    suspend fun setAccentColor(color: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCENT_COLOR] = color
        }
    }

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}