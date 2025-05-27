package com.example.targetassit.ui.home

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.targetassit.ui.common.CircularButton
import com.example.targetassit.ui.common.PanelCard
import com.example.targetassit.ui.theme.CardBackground
import com.example.targetassit.ui.theme.ErrorRed
import com.example.targetassit.ui.theme.SecondaryTeal
import com.example.targetassit.ui.theme.SuccessGreen
import com.example.targetassit.ui.theme.SurfaceLight
import com.example.targetassit.ui.theme.TargetAssitTheme
import com.example.targetassit.ui.theme.TextPrimary
import com.example.targetassit.ui.theme.TextSecondary
import com.example.targetassist.ui.home.TerminalVisualizer

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    // Use a direct reference to SystemInfoViewModel
    val context = LocalContext.current
    val systemInfoViewModel = viewModel<SystemInfoViewModel>()
    
    val uiState by viewModel.uiState.collectAsState()
    val currentDpi by viewModel.currentDpi.collectAsState()
    val permissionsNeeded by viewModel.permissionsNeeded.collectAsState()
    
    // For handling system info permissions
    var showPermissionDialog by remember { mutableStateOf(false) }
    
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        viewModel.onPermissionsResult(allGranted)
        
        if (allGranted) {
            Toast.makeText(context, "Permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Some permissions denied. System info may be limited.", Toast.LENGTH_LONG).show()
        }
    }
    
    // Check if we need to show permission dialog
    LaunchedEffect(permissionsNeeded) {
        showPermissionDialog = permissionsNeeded
    }
    
    // Permission dialog
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("Permissions Required") },
            text = { Text("Target Assist needs permissions to access system information for the terminal display.") },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDialog = false
                        permissionsLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        )
                    }
                ) {
                    Text("Grant Permissions")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionDialog = false }) {
                    Text("Later")
                }
            }
        )
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Left panel (larger panel) with Terminal Visualization instead of DPI Grid
            PanelCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                elevation = 2
            ) {
                TerminalVisualizer(dpi = currentDpi)
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
                    // First card - DPI
                    PanelCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "DPI",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
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
                            Text(
                                text = "Layout",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
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
                            Text(
                                text = "Settings",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
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
                                        shape = CircleShape
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