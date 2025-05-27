package com.example.targetassit.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.targetassit.data.preferences.UserPreferences
import com.example.targetassit.data.preferences.UserPreferencesRepository
import com.example.targetassit.ui.customhud.ButtonPosition
import com.example.targetassit.ui.sensitivity.SensitivitySettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userPreferencesRepository = UserPreferencesRepository.getInstance(application)
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collectLatest { preferences ->
                _uiState.value = _uiState.value.copy(
                    isOverlayEnabled = preferences.isOverlayEnabled,
                    gridDensity = preferences.gridDensity,
                    isGridVisible = preferences.isGridVisible,
                    isCenterMarkerVisible = preferences.isCenterMarkerVisible,
                    sensitivitySettings = preferences.sensitivitySettings,
                    hudButtonPositions = preferences.hudButtonPositions
                )
            }
        }
    }
    
    fun updateOverlayEnabled(enabled: Boolean) {
        userPreferencesRepository.updateOverlayEnabled(enabled)
    }
    
    fun updateGridDensity(density: Int) {
        userPreferencesRepository.updateGridDensity(density)
    }
    
    fun updateGridVisibility(visible: Boolean) {
        userPreferencesRepository.updateGridVisibility(visible)
    }
    
    fun updateCenterMarkerVisibility(visible: Boolean) {
        userPreferencesRepository.updateCenterMarkerVisibility(visible)
    }
    
    fun updateSensitivitySettings(settings: SensitivitySettings) {
        userPreferencesRepository.updateSensitivitySettings(settings)
    }
    
    fun updateHudButtonPositions(buttonPositions: List<ButtonPosition>) {
        userPreferencesRepository.updateHudButtonPositions(buttonPositions)
    }
    
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

data class HomeUiState(
    val isOverlayEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val gridDensity: Int = 30,
    val isGridVisible: Boolean = true,
    val isCenterMarkerVisible: Boolean = true,
    val sensitivitySettings: SensitivitySettings = SensitivitySettings(),
    val hudButtonPositions: List<ButtonPosition> = emptyList()
) 