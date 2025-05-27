package com.example.targetassit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.targetassit.ui.home.HomeScreen
import com.example.targetassit.ui.settings.SettingsScreen

object AppDestinations {
    const val HOME_ROUTE = "home"
    const val SETTINGS_ROUTE = "settings"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME_ROUTE
    ) {
        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen(
                navigateToSettings = {
                    navController.navigate(AppDestinations.SETTINGS_ROUTE)
                }
            )
        }
        
        composable(AppDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 