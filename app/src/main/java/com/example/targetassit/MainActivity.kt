package com.example.targetassit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.targetassit.ui.home.HomeScreen
import com.example.targetassit.ui.settings.SensitivityScreen
import com.example.targetassit.ui.settings.HudLayoutScreen
import com.example.targetassit.ui.theme.TargetAssitTheme
import com.example.targetassit.util.Constants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TargetAssitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onRequestOverlayPermission = {
                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:$packageName")
                            )
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onRequestOverlayPermission: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToSensitivity = { navController.navigate("sensitivity") },
                onNavigateToHudLayout = { navController.navigate("hudlayout") },
                onRequestOverlayPermission = onRequestOverlayPermission
            )
        }
        composable("sensitivity") {
            SensitivityScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("hudlayout") {
            HudLayoutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}