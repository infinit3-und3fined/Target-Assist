package com.example.targetassit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.targetassit.ui.home.HomeScreen
import com.example.targetassit.ui.home.HomeViewModel
import com.example.targetassit.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Sensitivity : Screen("sensitivity")
    object CustomHud : Screen("custom_hud")
    object About : Screen("about")
}

@Composable
fun AppNavigation(
    viewModel: HomeViewModel,
    startOverlayService: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartOverlayClick = startOverlayService,
                onSensitivityClick = { navController.navigate(Screen.Sensitivity.route) },
                onCustomHudClick = { navController.navigate(Screen.CustomHud.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onAboutClick = { navController.navigate(Screen.About.route) }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() },
                onGridDensityChange = { viewModel.updateGridDensity(it) },
                onGridVisibilityChange = { viewModel.updateGridVisibility(it) },
                onCenterMarkerVisibilityChange = { viewModel.updateCenterMarkerVisibility(it) }
            )
        }
        
        // Placeholder for other screens
        composable(Screen.Sensitivity.route) {
            // TODO: Implement Sensitivity screen
        }
        
        composable(Screen.CustomHud.route) {
            // TODO: Implement Custom HUD screen
        }
        
        composable(Screen.About.route) {
            // TODO: Implement About screen
        }
    }
} 