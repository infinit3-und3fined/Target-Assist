package com.example.targetassit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.targetassit.ui.home.HomeScreen
import com.example.targetassit.ui.theme.TargetAssitTheme
import com.example.targetassit.util.PermissionHelper

class MainActivity : ComponentActivity() {
    
    // Register for overlay permission result
    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (PermissionHelper.canDrawOverlays(this)) {
            Toast.makeText(this, "Overlay permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "Overlay permission denied. Some features will be unavailable.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Check for overlay permission
        checkOverlayPermission()
        
        setContent {
            TargetAssitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
    
    private fun checkOverlayPermission() {
        if (!PermissionHelper.canDrawOverlays(this)) {
            // Request permission
            val intent = PermissionHelper.createOverlayPermissionIntent(this)
            overlayPermissionLauncher.launch(intent)
        }
    }
}