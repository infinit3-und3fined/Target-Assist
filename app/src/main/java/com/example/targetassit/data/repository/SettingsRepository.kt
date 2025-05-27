package com.example.targetassit.data.repository

import android.content.Context
import android.provider.Settings
import com.example.targetassit.data.preferences.SettingsPreferences
import com.example.targetassit.domain.model.SensitivitySettings

class SettingsRepository(private val context: Context) {
    
    private val preferences = SettingsPreferences(context)
    
    fun getSensitivitySettings(): SensitivitySettings {
        return preferences.getSensitivitySettings()
    }
    
    fun saveSensitivitySettings(settings: SensitivitySettings) {
        preferences.saveSensitivitySettings(settings)
    }
    
    fun isOverlayPermissionGranted(): Boolean {
        return Settings.canDrawOverlays(context)
    }
    
    fun isOverlayEnabled(): Boolean {
        return preferences.isOverlayEnabled()
    }
    
    fun setOverlayEnabled(enabled: Boolean) {
        preferences.setOverlayEnabled(enabled)
    }
} 