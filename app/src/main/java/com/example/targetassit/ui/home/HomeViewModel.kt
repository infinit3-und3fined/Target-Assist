package com.example.targetassit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModel : ViewModel() {
    
    // Device info state
    private val _deviceInfo = MutableLiveData<DeviceInfo>()
    val deviceInfo: LiveData<DeviceInfo> = _deviceInfo
    
    // Service state
    private val _isServiceRunning = MutableLiveData<Boolean>()
    val isServiceRunning: LiveData<Boolean> = _isServiceRunning
    
    // Aim precision enabled state
    private val _isAimPrecisionEnabled = MutableLiveData<Boolean>()
    val isAimPrecisionEnabled: LiveData<Boolean> = _isAimPrecisionEnabled
    
    init {
        // Initialize with default values
        _isServiceRunning.value = false
        _isAimPrecisionEnabled.value = true
        
        // In a real app, we would fetch actual device info here
        _deviceInfo.value = DeviceInfo(
            androidVersion = "10",
            resolution = "768×120",
            dpi = 360,
            touchRate = 6
        )
    }
    
    fun toggleService() {
        _isServiceRunning.value = _isServiceRunning.value?.not() ?: false
    }
    
    fun toggleAimPrecision() {
        _isAimPrecisionEnabled.value = _isAimPrecisionEnabled.value?.not() ?: false
    }
}

data class DeviceInfo(
    val androidVersion: String,
    val resolution: String,
    val dpi: Int,
    val touchRate: Int
) 