package com.example.targetassist.ui.home

/**
 * Data class to hold system information to be displayed in the terminal
 */
data class SystemInfo(
    val deviceModel: String,
    val androidVersion: String,
    val cpuInfo: String,
    val cpuUsage: String,
    val memoryInfo: String,
    val screenInfo: String,
    val refreshRate: String,
    val dpi: String,
    val touchSamplingRate: String
) 