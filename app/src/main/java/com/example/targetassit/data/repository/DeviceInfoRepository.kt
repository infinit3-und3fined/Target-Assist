package com.example.targetassit.data.repository

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.example.targetassit.ui.home.DeviceInfo

class DeviceInfoRepository(private val context: Context) {
    
    fun getDeviceInfo(): DeviceInfo {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        
        val androidVersion = Build.VERSION.RELEASE
        val resolution = "${displayMetrics.widthPixels}×${displayMetrics.heightPixels}"
        val dpi = displayMetrics.densityDpi
        
        // Note: Touch rate is not directly accessible via Android API
        // In a real app, we would need to use a different approach or hardcode based on device model
        val touchRate = 6 // Default value, would need to be determined based on device model
        
        return DeviceInfo(
            androidVersion = androidVersion,
            resolution = resolution,
            dpi = dpi,
            touchRate = touchRate
        )
    }
} 