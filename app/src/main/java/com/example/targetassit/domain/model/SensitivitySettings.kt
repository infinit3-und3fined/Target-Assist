package com.example.targetassit.domain.model

data class SensitivitySettings(
    val dpi: Int = 360,
    val generalSensitivity: Int = 50,
    val redDotSensitivity: Int = 50,
    val x2ScopeSensitivity: Int = 50,
    val x4ScopeSensitivity: Int = 50
) 