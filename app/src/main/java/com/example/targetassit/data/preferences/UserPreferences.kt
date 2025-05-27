package com.example.targetassit.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.targetassit.ui.customhud.ButtonPosition
import com.example.targetassit.ui.sensitivity.SensitivitySettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import androidx.compose.ui.graphics.Color

data class UserPreferences(
    val gridDensity: Int = 30,
    val isGridVisible: Boolean = true,
    val isCenterMarkerVisible: Boolean = true,
    val isOverlayEnabled: Boolean = false,
    val sensitivitySettings: SensitivitySettings = SensitivitySettings(),
    val hudButtonPositions: List<ButtonPosition> = emptyList()
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
        
        // Read sensitivity settings
        val sensitivitySettings = readSensitivitySettings()
        
        // Read HUD button positions
        val hudButtonPositions = readHudButtonPositions()
        
        return UserPreferences(
            gridDensity = gridDensity,
            isGridVisible = isGridVisible,
            isCenterMarkerVisible = isCenterMarkerVisible,
            isOverlayEnabled = isOverlayEnabled,
            sensitivitySettings = sensitivitySettings,
            hudButtonPositions = hudButtonPositions
        )
    }
    
    private fun readSensitivitySettings(): SensitivitySettings {
        val general = sharedPreferences.getInt(KEY_SENSITIVITY_GENERAL, 100)
        val redDot = sharedPreferences.getInt(KEY_SENSITIVITY_RED_DOT, 100)
        val twoX = sharedPreferences.getInt(KEY_SENSITIVITY_TWO_X, 90)
        val fourX = sharedPreferences.getInt(KEY_SENSITIVITY_FOUR_X, 85)
        val awm = sharedPreferences.getInt(KEY_SENSITIVITY_AWM, 80)
        val freeLook = sharedPreferences.getInt(KEY_SENSITIVITY_FREE_LOOK, 100)
        
        return SensitivitySettings(
            general = general,
            redDot = redDot,
            twoX = twoX,
            fourX = fourX,
            awm = awm,
            freeLook = freeLook
        )
    }
    
    private fun readHudButtonPositions(): List<ButtonPosition> {
        val jsonString = sharedPreferences.getString(KEY_HUD_BUTTON_POSITIONS, null) ?: return getDefaultButtonPositions()
        
        return try {
            val jsonArray = JSONArray(jsonString)
            val buttonPositions = mutableListOf<ButtonPosition>()
            
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val x = jsonObject.getDouble("x").toFloat()
                val y = jsonObject.getDouble("y").toFloat()
                val size = jsonObject.getDouble("size").toFloat()
                val label = jsonObject.getString("label")
                val colorValue = jsonObject.getLong("color")
                
                buttonPositions.add(
                    ButtonPosition(
                        x = x,
                        y = y,
                        size = size,
                        label = label,
                        color = Color(colorValue)
                    )
                )
            }
            
            buttonPositions
        } catch (e: Exception) {
            getDefaultButtonPositions()
        }
    }
    
    private fun getDefaultButtonPositions(): List<ButtonPosition> {
        return listOf(
            ButtonPosition(100f, 300f, 60f, "Fire", Color(0xFFE53935).copy(alpha = 0.7f)),
            ButtonPosition(200f, 400f, 50f, "Jump", Color(0xFF43A047).copy(alpha = 0.7f)),
            ButtonPosition(300f, 350f, 55f, "Crouch", Color(0xFF1E88E5).copy(alpha = 0.7f)),
            ButtonPosition(400f, 450f, 65f, "Reload", Color(0xFFFFB300).copy(alpha = 0.7f))
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
    
    fun updateSensitivitySettings(settings: SensitivitySettings) {
        sharedPreferences.edit {
            putInt(KEY_SENSITIVITY_GENERAL, settings.general)
            putInt(KEY_SENSITIVITY_RED_DOT, settings.redDot)
            putInt(KEY_SENSITIVITY_TWO_X, settings.twoX)
            putInt(KEY_SENSITIVITY_FOUR_X, settings.fourX)
            putInt(KEY_SENSITIVITY_AWM, settings.awm)
            putInt(KEY_SENSITIVITY_FREE_LOOK, settings.freeLook)
        }
        updateUserPreferencesFlow()
    }
    
    fun updateHudButtonPositions(buttonPositions: List<ButtonPosition>) {
        val jsonArray = JSONArray()
        
        buttonPositions.forEach { button ->
            val jsonObject = JSONObject().apply {
                put("x", button.x)
                put("y", button.y)
                put("size", button.size)
                put("label", button.label)
                put("color", button.color.value)
            }
            jsonArray.put(jsonObject)
        }
        
        sharedPreferences.edit {
            putString(KEY_HUD_BUTTON_POSITIONS, jsonArray.toString())
        }
        updateUserPreferencesFlow()
    }
    
    private fun updateUserPreferencesFlow() {
        _userPreferencesFlow.value = readUserPreferences()
    }
    
    companion object {
        private const val PREFERENCES_NAME = "target_assist_preferences"
        
        // Grid and overlay settings
        private const val KEY_GRID_DENSITY = "grid_density"
        private const val KEY_GRID_VISIBLE = "grid_visible"
        private const val KEY_CENTER_MARKER_VISIBLE = "center_marker_visible"
        private const val KEY_OVERLAY_ENABLED = "overlay_enabled"
        
        // Sensitivity settings
        private const val KEY_SENSITIVITY_GENERAL = "sensitivity_general"
        private const val KEY_SENSITIVITY_RED_DOT = "sensitivity_red_dot"
        private const val KEY_SENSITIVITY_TWO_X = "sensitivity_two_x"
        private const val KEY_SENSITIVITY_FOUR_X = "sensitivity_four_x"
        private const val KEY_SENSITIVITY_AWM = "sensitivity_awm"
        private const val KEY_SENSITIVITY_FREE_LOOK = "sensitivity_free_look"
        
        // HUD layout settings
        private const val KEY_HUD_BUTTON_POSITIONS = "hud_button_positions"
        
        // Default values
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