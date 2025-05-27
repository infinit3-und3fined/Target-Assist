package com.example.targetassit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.targetassit.ui.home.HomeScreen
import com.example.targetassit.ui.settings.HudLayoutScreen
import com.example.targetassit.ui.settings.SensitivityScreen

@Composable
fun AppNavigation(
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