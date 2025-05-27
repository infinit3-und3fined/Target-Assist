package com.example.targetassit.data.preferences

import android.content.Context
import android.content.SharedPreferences

class SettingsPreferences(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )
    
    fun isAimPrecisionEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_AIM_PRECISION, true)
    }
    
    fun setAimPrecisionEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_AIM_PRECISION, enabled).apply()
    }
    
    fun getCustomDpi(): Int {
        return sharedPreferences.getInt(KEY_CUSTOM_DPI, -1)
    }
    
    fun setCustomDpi(dpi: Int) {
        sharedPreferences.edit().putInt(KEY_CUSTOM_DPI, dpi).apply()
    }
    
    companion object {
        private const val PREFERENCES_NAME = "target_assist_preferences"
        private const val KEY_AIM_PRECISION = "aim_precision"
        private const val KEY_CUSTOM_DPI = "custom_dpi"
    }
} 