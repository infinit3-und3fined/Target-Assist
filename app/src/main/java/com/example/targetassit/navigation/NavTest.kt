package com.example.targetassit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun SimpleNavTest() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "test",
        modifier = Modifier
    ) {
        composable("test") {
            // Empty composable
        }
    }
} 