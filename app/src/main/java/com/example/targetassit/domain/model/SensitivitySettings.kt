package com.example.targetassit.domain.model

import com.example.targetassit.util.Constants

data class SensitivitySettings(
    val dpi: Int = Constants.DEFAULT_DPI,
    val generalSensitivity: Float = 50f,
    val redDotSensitivity: Float = 50f,
    val twoXScopeSensitivity: Float = 50f,
    val fourXScopeSensitivity: Float = 50f
) 