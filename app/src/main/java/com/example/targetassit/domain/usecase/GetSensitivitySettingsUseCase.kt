package com.example.targetassit.domain.usecase

import com.example.targetassit.data.preferences.SettingsPreferences
import com.example.targetassit.domain.model.SensitivitySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSensitivitySettingsUseCase @Inject constructor(
    private val settingsPreferences: SettingsPreferences
) {
    operator fun invoke(): Flow<SensitivitySettings> {
        return combine(
            settingsPreferences.dpi,
            settingsPreferences.generalSensitivity,
            settingsPreferences.redDotSensitivity,
            settingsPreferences.x2ScopeSensitivity,
            settingsPreferences.x4ScopeSensitivity
        ) { dpi, generalSensitivity, redDotSensitivity, x2ScopeSensitivity, x4ScopeSensitivity ->
            SensitivitySettings(
                dpi = dpi,
                generalSensitivity = generalSensitivity,
                redDotSensitivity = redDotSensitivity,
                x2ScopeSensitivity = x2ScopeSensitivity,
                x4ScopeSensitivity = x4ScopeSensitivity
            )
        }
    }
} 