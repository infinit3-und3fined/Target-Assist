package com.example.targetassit.domain.usecase

import com.example.targetassit.data.preferences.SettingsPreferences
import com.example.targetassit.domain.model.SensitivitySettings
import javax.inject.Inject

class UpdateSensitivitySettingsUseCase @Inject constructor(
    private val settingsPreferences: SettingsPreferences
) {
    suspend operator fun invoke(settings: SensitivitySettings) {
        settingsPreferences.setDpi(settings.dpi)
        settingsPreferences.setGeneralSensitivity(settings.generalSensitivity)
        settingsPreferences.setRedDotSensitivity(settings.redDotSensitivity)
        settingsPreferences.setX2ScopeSensitivity(settings.x2ScopeSensitivity)
        settingsPreferences.setX4ScopeSensitivity(settings.x4ScopeSensitivity)
    }
} 