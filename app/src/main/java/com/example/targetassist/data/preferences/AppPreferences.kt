package com.example.targetassist.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPreferences(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    // DPI setting - kept for grid visualization
    private val _dpiFlow = MutableStateFlow(getDpi())
    val dpiFlow: StateFlow<Int> = _dpiFlow.asStateFlow()
    
    // Overlay active state
    private val _overlayActiveFlow = MutableStateFlow(isOverlayActive())
    val overlayActiveFlow: StateFlow<Boolean> = _overlayActiveFlow.asStateFlow()
    
    fun setDpi(dpi: Int) {
        prefs.edit().putInt(KEY_DPI, dpi).apply()
        _dpiFlow.value = dpi
    }
    
    fun getDpi(): Int = prefs.getInt(KEY_DPI, DEFAULT_DPI)
    
    fun setOverlayActive(active: Boolean) {
        prefs.edit().putBoolean(KEY_OVERLAY_ACTIVE, active).apply()
        _overlayActiveFlow.value = active
    }
    
    fun isOverlayActive(): Boolean = prefs.getBoolean(KEY_OVERLAY_ACTIVE, false)
    
    companion object {
        private const val PREFS_NAME = "target_assist_prefs"
        private const val KEY_DPI = "dpi"
        private const val KEY_OVERLAY_ACTIVE = "overlay_active"
        
        private const val DEFAULT_DPI = 360
        
        @Volatile
        private var INSTANCE: AppPreferences? = null
        
        fun getInstance(context: Context): AppPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppPreferences(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
} 