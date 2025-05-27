package com.example.targetassit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.targetassit.data.preferences.SettingsPreferences
import com.example.targetassit.domain.usecase.GetSensitivitySettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
    private val getSensitivitySettingsUseCase: GetSensitivitySettingsUseCase
) : ViewModel() {

    data class HomeUiState(
        val overlayEnabled: Boolean = false,
        val dpi: Int = 360
    )

    val uiState: StateFlow<HomeUiState> = combine(
        settingsPreferences.overlayEnabled,
        settingsPreferences.dpi
    ) { overlayEnabled, dpi ->
        HomeUiState(
            overlayEnabled = overlayEnabled,
            dpi = dpi
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun setOverlayEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setOverlayEnabled(enabled)
        }
    }
} 