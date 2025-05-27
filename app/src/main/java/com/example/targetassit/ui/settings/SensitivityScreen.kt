package com.example.targetassit.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.targetassit.domain.model.SensitivitySettings
import com.example.targetassit.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensitivityScreen(
    onNavigateBack: () -> Unit
) {
    var settings by remember {
        mutableStateOf(SensitivitySettings())
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensitivity Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // DPI Settings
            SensitivitySection(
                title = "DPI Settings",
                description = "Adjust your device's DPI (Dots Per Inch)"
            ) {
                Column {
                    Text(
                        "Current DPI: ${settings.dpi}",
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Slider(
                        value = settings.dpi.toFloat(),
                        onValueChange = { 
                            settings = settings.copy(dpi = it.toInt())
                        },
                        valueRange = Constants.MIN_DPI.toFloat()..Constants.MAX_DPI.toFloat(),
                        steps = ((Constants.MAX_DPI - Constants.MIN_DPI) / 20) - 1
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // DPI Grid Visualization
                    Text(
                        "DPI Grid Visualization",
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    DpiGridVisualization(dpi = settings.dpi)
                }
            }
            
            // General Sensitivity
            SensitivitySliderSection(
                title = "General Sensitivity",
                value = settings.generalSensitivity,
                onValueChange = { settings = settings.copy(generalSensitivity = it) }
            )
            
            // Red Dot Sensitivity
            SensitivitySliderSection(
                title = "Red Dot Sensitivity",
                value = settings.redDotSensitivity,
                onValueChange = { settings = settings.copy(redDotSensitivity = it) }
            )
            
            // 2x Scope Sensitivity
            SensitivitySliderSection(
                title = "2x Scope Sensitivity",
                value = settings.twoXScopeSensitivity,
                onValueChange = { settings = settings.copy(twoXScopeSensitivity = it) }
            )
            
            // 4x Scope Sensitivity
            SensitivitySliderSection(
                title = "4x Scope Sensitivity",
                value = settings.fourXScopeSensitivity,
                onValueChange = { settings = settings.copy(fourXScopeSensitivity = it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* Save settings */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save Settings")
            }
        }
    }
}

@Composable
fun SensitivitySection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

@Composable
fun SensitivitySliderSection(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    SensitivitySection(
        title = title,
        description = "Adjust your $title for optimal aiming"
    ) {
        Column {
            Text(
                "Value: $value",
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..100f
            )
        }
    }
}

@Composable
fun DpiGridVisualization(dpi: Int) {
    val gridSize = 5
    val cellSize = 200f / gridSize
    val spacing = (dpi / 120f).coerceIn(0.5f, 5f)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.dp)
        ) {
            repeat(gridSize) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.dp)
                ) {
                    repeat(gridSize) {
                        Box(
                            modifier = Modifier
                                .size(cellSize.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
                        )
                    }
                }
            }
        }
        
        // Crosshair overlay
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(2.dp, Color.Red, RoundedCornerShape(4.dp))
        )
    }
} 