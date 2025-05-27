package com.example.targetassit.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.targetassit.domain.model.SensitivitySettings
import com.example.targetassit.util.Constants

class SettingsPreferences(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "target_assist_prefs", 
        Context.MODE_PRIVATE
    )
    
    fun getSensitivitySettings(): SensitivitySettings {
        return SensitivitySettings(
            dpi = prefs.getInt(Constants.PREF_DPI_SETTING, Constants.DEFAULT_DPI),
            generalSensitivity = prefs.getFloat(Constants.PREF_GENERAL_SENSITIVITY, 50f),
            redDotSensitivity = prefs.getFloat(Constants.PREF_RED_DOT_SENSITIVITY, 50f),
            twoXScopeSensitivity = prefs.getFloat(Constants.PREF_2X_SCOPE_SENSITIVITY, 50f),
            fourXScopeSensitivity = prefs.getFloat(Constants.PREF_4X_SCOPE_SENSITIVITY, 50f)
        )
    }
    
    fun saveSensitivitySettings(settings: SensitivitySettings) {
        prefs.edit().apply {
            putInt(Constants.PREF_DPI_SETTING, settings.dpi)
            putFloat(Constants.PREF_GENERAL_SENSITIVITY, settings.generalSensitivity)
            putFloat(Constants.PREF_RED_DOT_SENSITIVITY, settings.redDotSensitivity)
            putFloat(Constants.PREF_2X_SCOPE_SENSITIVITY, settings.twoXScopeSensitivity)
            putFloat(Constants.PREF_4X_SCOPE_SENSITIVITY, settings.fourXScopeSensitivity)
            apply()
        }
    }
    
    fun isOverlayEnabled(): Boolean {
        return prefs.getBoolean("overlay_enabled", false)
    }
    
    fun setOverlayEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("overlay_enabled", enabled).apply()
    }
} 