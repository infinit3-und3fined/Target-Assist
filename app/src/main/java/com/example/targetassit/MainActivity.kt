package com.example.targetassit

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.targetassit.service.overlay.OverlayService
import com.example.targetassit.ui.home.HomeViewModel
import com.example.targetassit.ui.navigation.AppNavigation
import com.example.targetassit.ui.theme.TargetAssitTheme

class MainActivity : ComponentActivity() {
    
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.Factory(application)
    }
    
    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Settings.canDrawOverlays(this)) {
            startOverlayService()
        } else {
            Toast.makeText(
                this,
                "Overlay permission denied. Cannot start overlay.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TargetAssitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    AppNavigation(
                        viewModel = viewModel,
                        startOverlayService = { checkOverlayPermissionAndStartService() },
                        navController = navController
                    )
                }
            }
        }
    }
    
    private fun checkOverlayPermissionAndStartService() {
        if (Settings.canDrawOverlays(this)) {
            startOverlayService()
        } else {
            requestOverlayPermission()
        }
    }
    
    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        overlayPermissionLauncher.launch(intent)
    }
    
    private fun startOverlayService() {
        OverlayService.startService(this)
        viewModel.updateOverlayEnabled(true)
        Toast.makeText(this, "Overlay service started", Toast.LENGTH_SHORT).show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            OverlayService.stopService(this)
        }
    }
}