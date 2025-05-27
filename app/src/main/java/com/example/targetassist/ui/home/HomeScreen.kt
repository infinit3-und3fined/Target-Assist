package com.example.targetassist.ui.home

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.targetassist.service.overlay.OverlayService
import com.example.targetassist.ui.common.CircularButton
import com.example.targetassist.ui.common.PanelCard
import com.example.targetassist.ui.theme.CardBackground
import com.example.targetassist.ui.theme.ErrorRed
import com.example.targetassist.ui.theme.SecondaryTeal
import com.example.targetassist.ui.theme.SuccessGreen
import com.example.targetassist.ui.theme.SurfaceLight
import com.example.targetassist.ui.theme.TargetAssistTheme
import com.example.targetassist.ui.theme.TextPrimary
import com.example.targetassist.ui.theme.TextSecondary
import com.example.targetassist.util.PermissionHelper

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    systemInfoViewModel: SystemInfoViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var showPermissionDialog by remember { mutableStateOf(false) }
    val isOverlayRunning by homeViewModel.isOverlayRunning.collectAsState()
    
    // Check for system overlay permission
    LaunchedEffect(Unit) {
        if (!PermissionHelper.canDrawOverlays(context)) {
            showPermissionDialog = true
        }
    }
    
    // Monitor permission changes when app returns from settings
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (PermissionHelper.canDrawOverlays(context)) {
                    showPermissionDialog = false
                }
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = "Target Assist",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Terminal Visualizer
                TerminalVisualizer(
                    dpi = context.resources.displayMetrics.densityDpi,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Toggle Overlay Button
                Button(
                    onClick = { homeViewModel.toggleOverlayService() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (isOverlayRunning) "Stop Overlay" else "Start Overlay"
                    )
                }
            }
            
            // Permission Dialog
            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Permission Required") },
                    text = { Text("This app needs the overlay permission to display DPI information on top of other apps.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                                )
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Grant Permission")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showPermissionDialog = false }) {
                            Text("Later")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun HomeScreenPreview() {
    TargetAssistTheme {
        HomeScreen()
    }
} 