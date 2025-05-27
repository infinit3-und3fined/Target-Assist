package com.example.targetassit.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserPreferences(
    val gridDensity: Int = 30,
    val isGridVisible: Boolean = true,
    val isCenterMarkerVisible: Boolean = true,
    val isOverlayEnabled: Boolean = false
)

class UserPreferencesRepository(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )
    
    private val _userPreferencesFlow = MutableStateFlow(readUserPreferences())
    val userPreferencesFlow: StateFlow<UserPreferences> = _userPreferencesFlow.asStateFlow()
    
    private fun readUserPreferences(): UserPreferences {
        val gridDensity = sharedPreferences.getInt(KEY_GRID_DENSITY, DEFAULT_GRID_DENSITY)
        val isGridVisible = sharedPreferences.getBoolean(KEY_GRID_VISIBLE, DEFAULT_GRID_VISIBLE)
        val isCenterMarkerVisible = sharedPreferences.getBoolean(KEY_CENTER_MARKER_VISIBLE, DEFAULT_CENTER_MARKER_VISIBLE)
        val isOverlayEnabled = sharedPreferences.getBoolean(KEY_OVERLAY_ENABLED, DEFAULT_OVERLAY_ENABLED)
        
        return UserPreferences(
            gridDensity = gridDensity,
            isGridVisible = isGridVisible,
            isCenterMarkerVisible = isCenterMarkerVisible,
            isOverlayEnabled = isOverlayEnabled
        )
    }
    
    fun updateGridDensity(density: Int) {
        sharedPreferences.edit {
            putInt(KEY_GRID_DENSITY, density)
        }
        updateUserPreferencesFlow()
    }
    
    fun updateGridVisibility(visible: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_GRID_VISIBLE, visible)
        }
        updateUserPreferencesFlow()
    }
    
    fun updateCenterMarkerVisibility(visible: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_CENTER_MARKER_VISIBLE, visible)
        }
        updateUserPreferencesFlow()
    }
    
    fun updateOverlayEnabled(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_OVERLAY_ENABLED, enabled)
        }
        updateUserPreferencesFlow()
    }
    
    private fun updateUserPreferencesFlow() {
        _userPreferencesFlow.value = readUserPreferences()
    }
    
    companion object {
        private const val PREFERENCES_NAME = "target_assist_preferences"
        
        private const val KEY_GRID_DENSITY = "grid_density"
        private const val KEY_GRID_VISIBLE = "grid_visible"
        private const val KEY_CENTER_MARKER_VISIBLE = "center_marker_visible"
        private const val KEY_OVERLAY_ENABLED = "overlay_enabled"
        
        private const val DEFAULT_GRID_DENSITY = 30
        private const val DEFAULT_GRID_VISIBLE = true
        private const val DEFAULT_CENTER_MARKER_VISIBLE = true
        private const val DEFAULT_OVERLAY_ENABLED = false
        
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null
        
        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferencesRepository(context).also { INSTANCE = it }
            }
        }
    }
} 