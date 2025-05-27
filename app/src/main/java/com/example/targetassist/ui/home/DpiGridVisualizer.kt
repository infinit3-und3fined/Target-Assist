package com.example.targetassist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.targetassist.ui.theme.BackgroundDark
import com.example.targetassist.ui.theme.PrimaryBlue
import com.example.targetassist.ui.theme.SecondaryTeal
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DpiGridVisualizer(
    dpi: Int,
    modifier: Modifier = Modifier,
    systemInfoViewModel: SystemInfoViewModel? = null
) {
    // Create a local fallback for system info if the provided viewModel is null
    val localViewModel = try {
        systemInfoViewModel ?: viewModel()
    } catch (e: Exception) {
        null
    }
    
    // Create a default system info to use if viewModel is null
    val defaultSystemInfo = remember {
        SystemInfo(
            deviceModel = "Android Device",
            androidVersion = "Unknown",
            cpuInfo = "Unknown",
            cpuUsage = "N/A",
            memoryInfo = "N/A",
            screenInfo = "Unknown",
            refreshRate = "Unknown",
            dpi = "$dpi dpi",
            touchSamplingRate = "Unknown"
        )
    }
    
    // Get the system info from the viewModel if available, otherwise use default
    val systemInfo = localViewModel?.systemInfo?.collectAsState()?.value ?: defaultSystemInfo
    
    // For blinking cursor effect
    var showCursor by remember { mutableStateOf(true) }
    var currentTime by remember { mutableStateOf("") }
    
    // For scrolling effect
    val scrollState = rememberScrollState()
    
    LaunchedEffect(key1 = true) {
        while (true) {
            showCursor = !showCursor
            currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            delay(500) // Blink every 500ms
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Terminal header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = SecondaryTeal,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "System Information Terminal | ${currentTime}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            // Terminal content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                TerminalLine(
                    prefix = "root@${systemInfo.deviceModel.lowercase().replace(" ", "-")}:~#",
                    command = " neofetch",
                    prefixColor = Color(0xFF00FF00)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // ASCII art logo
                val asciiArt = """
                    ████████╗ █████╗ ██████╗  ██████╗ ███████╗████████╗
                    ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝ ██╔════╝╚══██╔══╝
                       ██║   ███████║██████╔╝██║  ███╗█████╗     ██║   
                       ██║   ██╔══██║██╔══██╗██║   ██║██╔══╝     ██║   
                       ██║   ██║  ██║██║  ██║╚██████╔╝███████╗   ██║   
                       ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   
                """.trimIndent()
                
                asciiArt.lines().forEach { line ->
                    Text(
                        text = line,
                        color = SecondaryTeal,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        letterSpacing = 0.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // System info in colorful terminal style
                InfoLine(label = "OS", value = "Android ${systemInfo.androidVersion}")
                InfoLine(label = "Host", value = systemInfo.deviceModel)
                InfoLine(label = "CPU", value = systemInfo.cpuInfo)
                InfoLine(label = "CPU Usage", value = systemInfo.cpuUsage)
                InfoLine(label = "Memory", value = systemInfo.memoryInfo)
                InfoLine(label = "Display", value = systemInfo.screenInfo)
                InfoLine(label = "Refresh Rate", value = systemInfo.refreshRate)
                InfoLine(label = "DPI", value = systemInfo.dpi)
                InfoLine(label = "Touch Rate", value = systemInfo.touchSamplingRate)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                TerminalLine(
                    prefix = "root@${systemInfo.deviceModel.lowercase().replace(" ", "-")}:~#",
                    command = " ls -la /system/app",
                    prefixColor = Color(0xFF00FF00)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "total 158",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Show some fake system files
                FileEntry(permissions = "drwxr-xr-x", owner = "system", name = "TargetAssistOverlay", isDir = true)
                FileEntry(permissions = "drwxr-xr-x", owner = "system", name = "SystemUI", isDir = true)
                FileEntry(permissions = "-rw-r--r--", owner = "system", name = "DisplayManager.apk")
                FileEntry(permissions = "-rw-r--r--", owner = "system", name = "TouchscreenService.apk")
                FileEntry(permissions = "-rw-r--r--", owner = "system", name = "FrameworkRes.apk")
                
                Spacer(modifier = Modifier.height(12.dp))
                
                TerminalLine(
                    prefix = "root@${systemInfo.deviceModel.lowercase().replace(" ", "-")}:~#",
                    command = " service list | grep display",
                    prefixColor = Color(0xFF00FF00)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "display: [com.android.server.display.DisplayManagerService]",
                    color = Color(0xFFADD8E6),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                TerminalLine(
                    prefix = "root@${systemInfo.deviceModel.lowercase().replace(" ", "-")}:~#",
                    command = " cat /proc/interrupts | grep -i touch",
                    prefixColor = Color(0xFF00FF00)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "175:        0          0     msmgpio  98  tsensor_int",
                    color = Color(0xFFADD8E6),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "root@${systemInfo.deviceModel.lowercase().replace(" ", "-")}:~#",
                        color = Color(0xFF00FF00),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                    
                    Text(
                        text = " ",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                    
                    // Blinking cursor
                    if (showCursor) {
                        Text(
                            text = "█",
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TerminalLine(
    prefix: String,
    command: String,
    prefixColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prefix,
            color = prefixColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
        
        Text(
            text = command,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    }
}

@Composable
fun InfoLine(
    label: String,
    value: String,
    labelColor: Color = Color(0xFFE6C07B)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label: ",
            color = labelColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = value,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    }
}

@Composable
fun FileEntry(
    permissions: String,
    owner: String = "system",
    name: String,
    isDir: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Text(
            text = permissions,
            color = Color(0xFFADD8E6),
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = owner,
            color = Color(0xFFE6C07B),
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = name,
            color = if (isDir) SecondaryTeal else Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    }
} 