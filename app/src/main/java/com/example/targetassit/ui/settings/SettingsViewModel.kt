package com.example.targetassit.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.targetassit.data.preferences.SettingsPreferences
import com.example.targetassit.domain.model.SensitivitySettings
import com.example.targetassit.domain.usecase.GetSensitivitySettingsUseCase
import com.example.targetassit.domain.usecase.UpdateSensitivitySettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSensitivitySettingsUseCase: GetSensitivitySettingsUseCase,
    private val updateSensitivitySettingsUseCase: UpdateSensitivitySettingsUseCase,
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {

    data class SettingsUiState(
        val dpi: Int = 360,
        val generalSensitivity: Int = 50,
        val redDotSensitivity: Int = 50,
        val x2ScopeSensitivity: Int = 50,
        val x4ScopeSensitivity: Int = 50
    )

    val uiState: StateFlow<SettingsUiState> = getSensitivitySettingsUseCase().map { settings ->
        SettingsUiState(
            dpi = settings.dpi,
            generalSensitivity = settings.generalSensitivity,
            redDotSensitivity = settings.redDotSensitivity,
            x2ScopeSensitivity = settings.x2ScopeSensitivity,
            x4ScopeSensitivity = settings.x4ScopeSensitivity
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun updateDpi(dpi: Int) {
        viewModelScope.launch {
            settingsPreferences.setDpi(dpi)
        }
    }

    fun updateGeneralSensitivity(sensitivity: Int) {
        viewModelScope.launch {
            settingsPreferences.setGeneralSensitivity(sensitivity)
        }
    }

    fun updateRedDotSensitivity(sensitivity: Int) {
        viewModelScope.launch {
            settingsPreferences.setRedDotSensitivity(sensitivity)
        }
    }

    fun updateX2ScopeSensitivity(sensitivity: Int) {
        viewModelScope.launch {
            settingsPreferences.setX2ScopeSensitivity(sensitivity)
        }
    }

    fun updateX4ScopeSensitivity(sensitivity: Int) {
        viewModelScope.launch {
            settingsPreferences.setX4ScopeSensitivity(sensitivity)
        }
    }
} 