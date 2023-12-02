package com.example.zvukus.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MAIN_SCREEN_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainScreen(
            toVisualTrack = navController::navigateToVisualTrackScreen,
            onBackClick = navController::popBackStack,
        )

        visualTrackScreen(
            onBackClick = navController::popBackStack,
        )
    }
}