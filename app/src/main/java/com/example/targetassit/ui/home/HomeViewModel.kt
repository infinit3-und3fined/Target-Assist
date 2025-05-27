package com.example.targetassit.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.targetassit.data.preferences.AppPreferences
import com.example.targetassit.service.overlay.OverlayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val appPreferences = AppPreferences.getInstance(application)
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    // Current device DPI from preferences
    val currentDpi = appPreferences.dpiFlow
    
    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isOverlayActive = appPreferences.isOverlayActive(),
                currentSensitivity = appPreferences.getSensitivity()
            )
        }
    }
    
    fun onStartOverlay() {
        viewModelScope.launch {
            appPreferences.setOverlayActive(true)
            _uiState.value = _uiState.value.copy(
                isOverlayActive = true
            )
            
            // Start the overlay service
            OverlayService.showOverlay(getApplication())
        }
    }
    
    fun onStopOverlay() {
        viewModelScope.launch {
            appPreferences.setOverlayActive(false)
            _uiState.value = _uiState.value.copy(
                isOverlayActive = false
            )
            
            // Stop the overlay service
            OverlayService.hideOverlay(getApplication())
        }
    }
    
    fun updateDpi(dpi: Int) {
        viewModelScope.launch {
            appPreferences.setDpi(dpi)
        }
    }
    
    fun updateSensitivity(sensitivity: Float) {
        viewModelScope.launch {
            appPreferences.setSensitivity(sensitivity)
            _uiState.value = _uiState.value.copy(
                currentSensitivity = sensitivity
            )
        }
    }
}

data class HomeUiState(
    val isOverlayActive: Boolean = false,
    val currentSensitivity: Float = 1.0f,
    val isLoading: Boolean = false,
    val error: String? = null
) 