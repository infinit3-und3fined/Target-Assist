package com.example.targetassit.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val DPI = intPreferencesKey("dpi")
        val GENERAL_SENSITIVITY = intPreferencesKey("general_sensitivity")
        val RED_DOT_SENSITIVITY = intPreferencesKey("red_dot_sensitivity")
        val X2_SCOPE_SENSITIVITY = intPreferencesKey("x2_scope_sensitivity")
        val X4_SCOPE_SENSITIVITY = intPreferencesKey("x4_scope_sensitivity")
        val OVERLAY_ENABLED = booleanPreferencesKey("overlay_enabled")
    }

    // DPI
    val dpi: Flow<Int> = dataStore.data.map { preferences ->
        preferences[DPI] ?: 360 // Default DPI
    }

    suspend fun setDpi(value: Int) {
        dataStore.edit { preferences ->
            preferences[DPI] = value
        }
    }

    // General Sensitivity
    val generalSensitivity: Flow<Int> = dataStore.data.map { preferences ->
        preferences[GENERAL_SENSITIVITY] ?: 50 // Default value
    }

    suspend fun setGeneralSensitivity(value: Int) {
        dataStore.edit { preferences ->
            preferences[GENERAL_SENSITIVITY] = value
        }
    }

    // Red Dot Sensitivity
    val redDotSensitivity: Flow<Int> = dataStore.data.map { preferences ->
        preferences[RED_DOT_SENSITIVITY] ?: 50 // Default value
    }

    suspend fun setRedDotSensitivity(value: Int) {
        dataStore.edit { preferences ->
            preferences[RED_DOT_SENSITIVITY] = value
        }
    }

    // 2X Scope Sensitivity
    val x2ScopeSensitivity: Flow<Int> = dataStore.data.map { preferences ->
        preferences[X2_SCOPE_SENSITIVITY] ?: 50 // Default value
    }

    suspend fun setX2ScopeSensitivity(value: Int) {
        dataStore.edit { preferences ->
            preferences[X2_SCOPE_SENSITIVITY] = value
        }
    }

    // 4X Scope Sensitivity
    val x4ScopeSensitivity: Flow<Int> = dataStore.data.map { preferences ->
        preferences[X4_SCOPE_SENSITIVITY] ?: 50 // Default value
    }

    suspend fun setX4ScopeSensitivity(value: Int) {
        dataStore.edit { preferences ->
            preferences[X4_SCOPE_SENSITIVITY] = value
        }
    }

    // Overlay Enabled
    val overlayEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[OVERLAY_ENABLED] ?: false // Default value
    }

    suspend fun setOverlayEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[OVERLAY_ENABLED] = enabled
        }
    }
} 