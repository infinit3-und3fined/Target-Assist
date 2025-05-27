package com.example.targetassit.ui.sensitivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.targetassit.ui.theme.TargetAssitTheme

data class SensitivitySettings(
    val general: Int = 100,
    val redDot: Int = 100,
    val twoX: Int = 90,
    val fourX: Int = 85,
    val awm: Int = 80,
    val freeLook: Int = 100
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensitivityScreen(
    onBackClick: () -> Unit = {},
    onSensitivityChange: (SensitivitySettings) -> Unit = {}
) {
    var sensitivitySettings by remember { mutableStateOf(SensitivitySettings()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensitivity Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Adjust Sensitivity Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Optimize your aim by adjusting sensitivity values for different scopes",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // General sensitivity
            SensitivitySlider(
                title = "General Sensitivity",
                value = sensitivitySettings.general,
                onValueChange = { 
                    sensitivitySettings = sensitivitySettings.copy(general = it)
                    onSensitivityChange(sensitivitySettings)
                }
            )
            
            // Red Dot sensitivity
            SensitivitySlider(
                title = "Red Dot Sensitivity",
                value = sensitivitySettings.redDot,
                onValueChange = { 
                    sensitivitySettings = sensitivitySettings.copy(redDot = it)
                    onSensitivityChange(sensitivitySettings)
                }
            )
            
            // 2X Scope sensitivity
            SensitivitySlider(
                title = "2X Scope Sensitivity",
                value = sensitivitySettings.twoX,
                onValueChange = { 
                    sensitivitySettings = sensitivitySettings.copy(twoX = it)
                    onSensitivityChange(sensitivitySettings)
                }
            )
            
            // 4X Scope sensitivity
            SensitivitySlider(
                title = "4X Scope Sensitivity",
                value = sensitivitySettings.fourX,
                onValueChange = { 
                    sensitivitySettings = sensitivitySettings.copy(fourX = it)
                    onSensitivityChange(sensitivitySettings)
                }
            )
            
            // AWM Scope sensitivity
            SensitivitySlider(
                title = "AWM Scope Sensitivity",
                value = sensitivitySettings.awm,
                onValueChange = { 
                    sensitivitySettings = sensitivitySettings.copy(awm = it)
                    onSensitivityChange(sensitivitySettings)
                }
            )
            
            // Free Look sensitivity
            SensitivitySlider(
                title = "Free Look Sensitivity",
                value = sensitivitySettings.freeLook,
                onValueChange = { 
                    sensitivitySettings = sensitivitySettings.copy(freeLook = it)
                    onSensitivityChange(sensitivitySettings)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Tip: Lower sensitivity for scopes helps with precision aiming at distance",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
fun SensitivitySlider(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = value.toString(),
                fontSize = 16.sp,
                color = Color(0xFF3949AB),
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 10f..200f,
            steps = 190,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun SensitivityScreenPreview() {
    TargetAssitTheme {
        SensitivityScreen()
    }
} 