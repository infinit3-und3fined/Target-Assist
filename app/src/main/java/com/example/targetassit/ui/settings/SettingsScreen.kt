package com.example.targetassit.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.targetassit.ui.common.DpiGridView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoStartOverlay by remember { mutableStateOf(false) }

    if (navigateBack != {}) {
        // This is the Sensitivity Settings screen
        SensitivitySettingsContent(
            uiState = uiState,
            viewModel = viewModel,
            navigateBack = navigateBack
        )
    } else {
        // This is the General Settings screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // App Settings
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "App Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    SettingsSwitchItem(
                        title = "Dark Theme",
                        description = "Enable dark theme for the app",
                        checked = darkThemeEnabled,
                        onCheckedChange = { darkThemeEnabled = it }
                    )
                    
                    Divider()
                    
                    SettingsSwitchItem(
                        title = "Notifications",
                        description = "Enable notifications from Target Assist",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }
            
            // Overlay Settings
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Overlay Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    SettingsSwitchItem(
                        title = "Auto-start Overlay",
                        description = "Automatically start overlay when Free Fire is launched",
                        checked = autoStartOverlay,
                        onCheckedChange = { autoStartOverlay = it }
                    )
                    
                    Divider()
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Overlay Opacity",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = "75%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Slider(
                        value = 0.75f,
                        onValueChange = { /* Update opacity */ },
                        valueRange = 0.1f..1f,
                        steps = 9
                    )
                }
            }
            
            // About App
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Target Assist v1.0",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Text(
                        text = "Designed for Free Fire players to optimize their gameplay",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensitivitySettingsContent(
    uiState: SettingsViewModel.SettingsUiState,
    viewModel: SettingsViewModel,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensitivity Settings") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // DPI Grid Visualization
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "DPI Visualization",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            factory = { context ->
                                DpiGridView(context).apply {
                                    dpi = uiState.dpi
                                    gridSize = 200
                                }
                            },
                            update = { view ->
                                view.dpi = uiState.dpi
                            }
                        )
                    }
                    
                    Text(
                        text = "Current DPI: ${uiState.dpi}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            
            // DPI Setting
            SettingSlider(
                title = "DPI",
                value = uiState.dpi,
                valueRange = 100f..1000f,
                onValueChange = { viewModel.updateDpi(it.toInt()) }
            )
            
            // General Sensitivity
            SettingSlider(
                title = "General Sensitivity",
                value = uiState.generalSensitivity,
                valueRange = 0f..100f,
                onValueChange = { viewModel.updateGeneralSensitivity(it.toInt()) }
            )
            
            // Red Dot Sensitivity
            SettingSlider(
                title = "Red Dot Sensitivity",
                value = uiState.redDotSensitivity,
                valueRange = 0f..100f,
                onValueChange = { viewModel.updateRedDotSensitivity(it.toInt()) }
            )
            
            // 2X Scope Sensitivity
            SettingSlider(
                title = "2X Scope Sensitivity",
                value = uiState.x2ScopeSensitivity,
                valueRange = 0f..100f,
                onValueChange = { viewModel.updateX2ScopeSensitivity(it.toInt()) }
            )
            
            // 4X Scope Sensitivity
            SettingSlider(
                title = "4X Scope Sensitivity",
                value = uiState.x4ScopeSensitivity,
                valueRange = 0f..100f,
                onValueChange = { viewModel.updateX4ScopeSensitivity(it.toInt()) }
            )
        }
    }
}

@Composable
fun SettingSlider(
    title: String,
    value: Int,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    var sliderPosition by remember { mutableIntStateOf(value) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = sliderPosition.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Slider(
                value = sliderPosition.toFloat(),
                onValueChange = { 
                    sliderPosition = it.toInt()
                },
                onValueChangeFinished = {
                    onValueChange(sliderPosition.toFloat())
                },
                valueRange = valueRange,
                steps = 0
            )
        }
    }
} 