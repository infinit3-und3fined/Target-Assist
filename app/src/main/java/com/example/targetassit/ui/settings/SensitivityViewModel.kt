package com.example.targetassit.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.targetassit.data.repository.SettingsRepository
import com.example.targetassit.domain.model.SensitivitySettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SensitivityViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = SettingsRepository(application)
    
    private val _settings = MutableStateFlow(SensitivitySettings())
    val settings: StateFlow<SensitivitySettings> = _settings.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        _settings.value = repository.getSensitivitySettings()
    }
    
    fun updateDpi(dpi: Int) {
        _settings.value = _settings.value.copy(dpi = dpi)
    }
    
    fun updateGeneralSensitivity(value: Float) {
        _settings.value = _settings.value.copy(generalSensitivity = value)
    }
    
    fun updateRedDotSensitivity(value: Float) {
        _settings.value = _settings.value.copy(redDotSensitivity = value)
    }
    
    fun update2xScopeSensitivity(value: Float) {
        _settings.value = _settings.value.copy(twoXScopeSensitivity = value)
    }
    
    fun update4xScopeSensitivity(value: Float) {
        _settings.value = _settings.value.copy(fourXScopeSensitivity = value)
    }
    
    fun saveSettings() {
        viewModelScope.launch {
            repository.saveSensitivitySettings(_settings.value)
        }
    }
} 