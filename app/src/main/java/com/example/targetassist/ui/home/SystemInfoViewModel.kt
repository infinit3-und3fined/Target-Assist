package com.example.targetassist.ui.home

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.DecimalFormat

class SystemInfoViewModel : ViewModel() {
    
    private val _systemInfo = MutableStateFlow<SystemInfo?>(null)
    val systemInfo: StateFlow<SystemInfo?> = _systemInfo.asStateFlow()
    
    // Cache for CPU info to avoid repeated parsing
    private var cpuInfoCache = ""
    
    fun startCollectingSystemInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val systemInfo = collectSystemInfo(context)
                _systemInfo.value = systemInfo
                delay(1000) // Update every second
            }
        }
    }
    
    private fun collectSystemInfo(context: Context): SystemInfo {
        return SystemInfo(
            deviceModel = getDeviceModel(),
            androidVersion = getAndroidVersion(),
            cpuInfo = getCpuInfo(),
            cpuUsage = getCpuUsage(),
            memoryInfo = getMemoryInfo(context),
            screenInfo = getScreenInfo(context),
            refreshRate = getRefreshRate(context),
            dpi = getDpi(context),
            touchSamplingRate = getTouchSamplingRate() // This is often not available through APIs
        )
    }
    
    private fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }
    
    private fun getAndroidVersion(): String {
        return "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }
    
    private fun getCpuInfo(): String {
        if (cpuInfoCache.isNotEmpty()) {
            return cpuInfoCache
        }
        
        try {
            val reader = BufferedReader(FileReader("/proc/cpuinfo"))
            var line: String?
            val sb = StringBuilder()
            var processorCount = 0
            var modelName = ""
            
            while (reader.readLine().also { line = it } != null) {
                if (line?.contains("processor") == true) {
                    processorCount++
                }
                if (line?.contains("model name") == true || line?.contains("Processor") == true) {
                    modelName = line?.split(":")?.get(1)?.trim() ?: "Unknown"
                    break
                }
            }
            reader.close()
            
            cpuInfoCache = "$modelName ($processorCount cores)"
            return cpuInfoCache
        } catch (e: IOException) {
            return "Unknown CPU"
        }
    }
    
    private fun getCpuUsage(): String {
        try {
            // Read CPU stats
            val reader1 = BufferedReader(FileReader("/proc/stat"))
            val cpu1 = reader1.readLine()
            reader1.close()
            
            val cpuParts1 = cpu1.split("\\s+".toRegex()).drop(1)
            val idle1 = cpuParts1[3].toLong()
            val total1 = cpuParts1.take(7).sumOf { it.toLong() }
            
            // Wait a bit and read again
            Thread.sleep(500)
            
            val reader2 = BufferedReader(FileReader("/proc/stat"))
            val cpu2 = reader2.readLine()
            reader2.close()
            
            val cpuParts2 = cpu2.split("\\s+".toRegex()).drop(1)
            val idle2 = cpuParts2[3].toLong()
            val total2 = cpuParts2.take(7).sumOf { it.toLong() }
            
            // Calculate usage
            val idleDelta = idle2 - idle1
            val totalDelta = total2 - total1
            val usagePercent = 100.0 * (1.0 - idleDelta.toDouble() / totalDelta.toDouble())
            
            val df = DecimalFormat("#.##")
            return "${df.format(usagePercent)}%"
        } catch (e: Exception) {
            return "N/A"
        }
    }
    
    private fun getMemoryInfo(context: Context): String {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        val availableMegs = memoryInfo.availMem / 1048576L
        val totalMegs = memoryInfo.totalMem / 1048576L
        val usedMegs = totalMegs - availableMegs
        val percentUsed = (usedMegs.toDouble() / totalMegs.toDouble() * 100).toInt()
        
        return "$usedMegs MB / $totalMegs MB ($percentUsed%)"
    }
    
    private fun getScreenInfo(context: Context): String {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = context.display
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        }
        
        return "${displayMetrics.widthPixels}×${displayMetrics.heightPixels}"
    }
    
    private fun getRefreshRate(context: Context): String {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = context.display
            val refreshRate = display?.refreshRate ?: 60f
            "${refreshRate.toInt()} Hz"
        } else {
            @Suppress("DEPRECATION")
            val refreshRate = windowManager.defaultDisplay.refreshRate
            "${refreshRate.toInt()} Hz"
        }
    }
    
    private fun getDpi(context: Context): String {
        val displayMetrics = context.resources.displayMetrics
        return "${displayMetrics.densityDpi} dpi"
    }
    
    private fun getTouchSamplingRate(): String {
        // This is hard to get programmatically on most devices
        // Some devices store it in /proc or /sys, but it's not standardized
        
        // Try common paths where some devices store this info
        val possiblePaths = listOf(
            "/sys/class/touch/touch_dev/touch_rate",
            "/proc/touchpanel/report_rate",
            "/sys/devices/virtual/touch/touch_dev/touch_rate"
        )
        
        for (path in possiblePaths) {
            try {
                val file = File(path)
                if (file.exists()) {
                    val reader = BufferedReader(FileReader(file))
                    val rate = reader.readLine()?.trim()
                    reader.close()
                    
                    if (rate != null && rate.isNotEmpty()) {
                        // Try to parse as number and return with Hz
                        return try {
                            "${rate.toInt()} Hz"
                        } catch (e: Exception) {
                            rate
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore and try next path
            }
        }
        
        // Default value if we couldn't find it
        return "Unknown"
    }
}

@Composable
fun rememberSystemInfo(viewModel: SystemInfoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()): SystemInfo {
    val context = LocalContext.current
    val systemInfo by viewModel.systemInfo.collectAsState(initial = null)
    
    // Start collecting when this composable is first used
    if (viewModel.systemInfo.value == null) {
        viewModel.startCollectingSystemInfo(context)
    }
    
    // Return default info while loading or if there's an error
    return systemInfo ?: SystemInfo(
        deviceModel = Build.MODEL,
        androidVersion = Build.VERSION.RELEASE,
        cpuInfo = "Loading...",
        cpuUsage = "Loading...",
        memoryInfo = "Loading...",
        screenInfo = "Loading...",
        refreshRate = "Loading...",
        dpi = "${context.resources.displayMetrics.densityDpi} dpi",
        touchSamplingRate = "Loading..."
    )
} 