package com.example.targetassit.ui.home

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.RandomAccessFile

class SystemInfoViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _systemInfo = MutableStateFlow(SystemInfo())
    val systemInfo: StateFlow<SystemInfo> = _systemInfo.asStateFlow()
    
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 1000L // Update every second
    
    init {
        collectSystemInfo()
        startPeriodicUpdates()
    }
    
    private fun startPeriodicUpdates() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateDynamicInfo()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }
    
    private fun collectSystemInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>()
            
            try {
                val systemInfo = SystemInfo(
                    deviceModel = Build.MODEL,
                    androidVersion = Build.VERSION.RELEASE,
                    cpuInfo = getCpuInfo(),
                    cpuUsage = getCpuUsage(),
                    memoryInfo = getMemoryInfo(),
                    screenInfo = getScreenInfo(context),
                    refreshRate = getRefreshRate(context),
                    dpi = getDpi(context),
                    touchSamplingRate = getTouchSamplingRate()
                )
                
                _systemInfo.value = systemInfo
            } catch (e: Exception) {
                // Fallback to safe values if we encounter any exceptions
                _systemInfo.value = createFallbackSystemInfo(context)
            }
        }
    }
    
    private fun createFallbackSystemInfo(context: Context): SystemInfo {
        return SystemInfo(
            deviceModel = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            cpuInfo = Build.HARDWARE,
            cpuUsage = "N/A",
            memoryInfo = "N/A",
            screenInfo = try {
                val metrics = context.resources.displayMetrics
                "${metrics.widthPixels} x ${metrics.heightPixels}"
            } catch (e: Exception) {
                "Unknown"
            },
            refreshRate = "60 Hz",
            dpi = try {
                "${context.resources.displayMetrics.densityDpi} dpi"
            } catch (e: Exception) {
                "Unknown"
            },
            touchSamplingRate = "120 Hz (estimated)"
        )
    }
    
    private fun updateDynamicInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentInfo = _systemInfo.value
                _systemInfo.value = currentInfo.copy(
                    cpuUsage = getCpuUsage(),
                    memoryInfo = getMemoryInfo()
                )
            } catch (e: Exception) {
                // Ignore errors during updates
            }
        }
    }
    
    private fun getCpuInfo(): String {
        return try {
            val reader = BufferedReader(FileReader("/proc/cpuinfo"))
            var line: String?
            var processor = ""
            var hardware = ""
            
            while (reader.readLine().also { line = it } != null) {
                if (line?.startsWith("processor") == true) {
                    processor = line?.substringAfter(":") ?: ""
                }
                if (line?.startsWith("Hardware") == true) {
                    hardware = line?.substringAfter(":") ?: ""
                    break
                }
            }
            reader.close()
            
            if (hardware.isNotBlank()) {
                hardware.trim()
            } else if (Build.HARDWARE.isNotBlank()) {
                Build.HARDWARE
            } else {
                Build.BOARD
            }
        } catch (e: Exception) {
            Build.HARDWARE
        }
    }
    
    private fun getCpuUsage(): String {
        return try {
            val reader1 = RandomAccessFile("/proc/stat", "r")
            val cpu1 = reader1.readLine().split("\\s+".toRegex())
            val idle1 = cpu1[4].toLong()
            val total1 = cpu1.subList(1, cpu1.size).sumOf { it.toLong() }
            Thread.sleep(100)
            
            reader1.seek(0)
            val cpu2 = reader1.readLine().split("\\s+".toRegex())
            val idle2 = cpu2[4].toLong()
            val total2 = cpu2.subList(1, cpu2.size).sumOf { it.toLong() }
            reader1.close()
            
            val idleDiff = idle2 - idle1
            val totalDiff = total2 - total1
            val usage = 100.0 * (1.0 - idleDiff.toDouble() / totalDiff.toDouble())
            
            "${usage.toInt()}%"
        } catch (e: Exception) {
            "N/A"
        }
    }
    
    private fun getMemoryInfo(): String {
        return try {
            val runtime = Runtime.getRuntime()
            val totalMB = runtime.totalMemory() / (1024 * 1024)
            val freeMB = runtime.freeMemory() / (1024 * 1024)
            val usedMB = totalMB - freeMB
            
            "$usedMB MB / $totalMB MB"
        } catch (e: Exception) {
            "N/A"
        }
    }
    
    private fun getScreenInfo(context: Context): String {
        return try {
            val metrics = context.resources.displayMetrics
            "${metrics.widthPixels} x ${metrics.heightPixels}"
        } catch (e: Exception) {
            "Unknown"
        }
    }
    
    private fun getRefreshRate(context: Context): String {
        return try {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            
            val refreshRate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val display = context.display
                display?.refreshRate ?: 60f
            } else {
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.refreshRate
            }
            
            "${refreshRate.toInt()} Hz"
        } catch (e: Exception) {
            "60 Hz"
        }
    }
    
    private fun getDpi(context: Context): String {
        return try {
            val metrics = context.resources.displayMetrics
            "${metrics.densityDpi} dpi"
        } catch (e: Exception) {
            "Unknown"
        }
    }
    
    private fun getTouchSamplingRate(): String {
        // There's no reliable API for touch sampling rate, so we estimate based on device
        return try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> "240 Hz" // Android 12+
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> "180 Hz" // Android 11
                else -> "120 Hz" // Older Android versions
            }
        } catch (e: Exception) {
            "120 Hz (estimated)"
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }
}

data class SystemInfo(
    val deviceModel: String = "Unknown Device",
    val androidVersion: String = "Unknown",
    val cpuInfo: String = "Unknown",
    val cpuUsage: String = "0%",
    val memoryInfo: String = "0 MB / 0 MB",
    val screenInfo: String = "0 x 0",
    val refreshRate: String = "0 Hz",
    val dpi: String = "0 dpi",
    val touchSamplingRate: String = "Unknown"
) 