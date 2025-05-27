package com.example.targetassit.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.targetassit.ui.common.CircularButton
import com.example.targetassit.ui.common.PanelCard
import com.example.targetassit.ui.theme.TargetAssitTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentDpi by viewModel.currentDpi.collectAsState()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Left panel (larger panel) with DPI Grid Visualization
            PanelCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                elevation = 4
            ) {
                DpiGridVisualizer(dpi = currentDpi)
            }
            
            // Right panel (contains top row and bottom section)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                // Top row with three equal cards
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                ) {
                    // First card - Sensitivity
                    PanelCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Sensitivity")
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text("DPI: $currentDpi")
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // DPI Slider
                            Slider(
                                value = currentDpi.toFloat(),
                                onValueChange = { viewModel.updateDpi(it.toInt()) },
                                valueRange = 160f..800f,
                                steps = 0
                            )
                        }
                    }
                    
                    // Second card - Layout
                    PanelCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Layout")
                        }
                    }
                    
                    // Third card - Settings
                    PanelCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Settings")
                        }
                    }
                }
                
                // Bottom section with circular button
                PanelCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Status text
                        Text(
                            text = if (uiState.isOverlayActive) "Overlay is active" else "Overlay is inactive",
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        )
                        
                        // Circular button with shadow
                        CircularButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            size = 100.dp,
                            backgroundColor = if (uiState.isOverlayActive) 
                                Color(0xFFE57373) else MaterialTheme.colorScheme.primary,
                            icon = if (uiState.isOverlayActive) 
                                Icons.Filled.Close else Icons.Filled.PlayArrow,
                            onClick = { 
                                if (uiState.isOverlayActive) {
                                    viewModel.onStopOverlay()
                                } else {
                                    viewModel.onStartOverlay()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun HomeScreenPreview() {
    TargetAssitTheme {
        HomeScreen()
    }
} 