package com.example.targetassit.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.targetassit.ui.common.CircularButton
import com.example.targetassit.ui.common.PanelCard
import com.example.targetassit.ui.theme.ErrorRed
import com.example.targetassit.ui.theme.SecondaryTeal
import com.example.targetassit.ui.theme.SuccessGreen
import com.example.targetassit.ui.theme.SurfaceLight
import com.example.targetassit.ui.theme.TargetAssitTheme
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
            
            // Right panel with just activation button
            PanelCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                elevation = 2
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
                    
                    // Circular button with shadow
                    CircularButton(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        size = 90.dp,
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

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun HomeScreenPreview() {
    TargetAssitTheme {
        HomeScreen()
    }
} 