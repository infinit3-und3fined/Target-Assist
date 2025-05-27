package com.example.targetassist.ui.home

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.targetassist.data.preferences.AppPreferences
import com.example.targetassist.service.overlay.OverlayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.content.Intent

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val appPreferences = AppPreferences.getInstance(application)
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private val _permissionsNeeded = MutableStateFlow(false)
    val permissionsNeeded: StateFlow<Boolean> = _permissionsNeeded.asStateFlow()
    
    // Current device DPI from preferences - needed for grid visualization only
    val currentDpi = appPreferences.dpiFlow
    
    private val _isOverlayRunning = MutableStateFlow(OverlayService.isRunning)
    val isOverlayRunning: StateFlow<Boolean> = _isOverlayRunning.asStateFlow()
    
    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isOverlayActive = appPreferences.isOverlayActive()
            )
            
            checkPermissions()
        }
    }
    
    private fun checkPermissions() {
        val context = getApplication<Application>()
        val hasPhoneStatePermission = ActivityCompat.checkSelfPermission(
            context, 
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
        
        val hasStoragePermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        
        _permissionsNeeded.value = !hasPhoneStatePermission || !hasStoragePermission
    }
    
    fun onPermissionsResult(allGranted: Boolean) {
        _permissionsNeeded.value = !allGranted
    }
    
    fun startOverlayService() {
        val context = getApplication<Application>()
        val intent = Intent(context, OverlayService::class.java)
        context.startService(intent)
        _isOverlayRunning.value = true
    }
    
    fun stopOverlayService() {
        val context = getApplication<Application>()
        val intent = Intent(context, OverlayService::class.java)
        context.stopService(intent)
        _isOverlayRunning.value = false
    }
    
    fun toggleOverlayService() {
        if (_isOverlayRunning.value) {
            stopOverlayService()
        } else {
            startOverlayService()
        }
    }
    
    // Keep this function just for the grid visualization
    fun updateDpi(dpi: Int) {
        viewModelScope.launch {
            appPreferences.setDpi(dpi)
        }
    }
}

data class HomeUiState(
    val isOverlayActive: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) 