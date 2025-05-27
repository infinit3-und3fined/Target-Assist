package com.example.targetassit.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.targetassit.ui.common.CircularButton
import com.example.targetassit.ui.common.PanelCard
import com.example.targetassit.ui.theme.CardBackground
import com.example.targetassit.ui.theme.ErrorRed
import com.example.targetassit.ui.theme.PrimaryBlue
import com.example.targetassit.ui.theme.SecondaryTeal
import com.example.targetassit.ui.theme.SuccessGreen
import com.example.targetassit.ui.theme.SurfaceLight
import com.example.targetassit.ui.theme.TargetAssitTheme
import com.example.targetassit.ui.theme.TextPrimary
import com.example.targetassit.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentDpi by viewModel.currentDpi.collectAsState()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Left panel (larger panel) with DPI Grid Visualization
            PanelCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                elevation = 2
            ) {
                DpiGridVisualizer(dpi = currentDpi)
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Right panel (contains top row and bottom section)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
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
                            Text(
                                text = "DPI",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Empty box for now
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Second card - Layout
                    PanelCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Layout",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            // Empty box for now
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Third card - Settings
                    PanelCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Settings",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            // Empty box for now
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Bottom section with circular button
                PanelCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Status indicator - small colored dot with text
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = if (uiState.isOverlayActive) SuccessGreen else TextSecondary,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = if (uiState.isOverlayActive) "Overlay active" else "Overlay inactive",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                        
                        // DPI Slider in the center
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sensitivity: $currentDpi DPI",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // DPI Slider
                            Slider(
                                value = currentDpi.toFloat(),
                                onValueChange = { viewModel.updateDpi(it.toInt()) },
                                valueRange = 160f..800f,
                                steps = 0,
                                colors = SliderDefaults.colors(
                                    thumbColor = PrimaryBlue,
                                    activeTrackColor = PrimaryBlue,
                                    inactiveTrackColor = PrimaryBlue.copy(alpha = 0.3f)
                                )
                            )
                        }
                        
                        // Circular button with shadow
                        CircularButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            size = 70.dp,
                            backgroundColor = if (uiState.isOverlayActive) 
                                ErrorRed else SecondaryTeal,
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