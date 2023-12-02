package com.example.zvukus.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import com.example.zvukus.screen.visual.visualTrackRouter

const val VISUAL_TRACK_SCREEN_ROUTE = "VISUAL_TRACK_SCREEN_ROUTE"

fun NavController.navigateToVisualTrackScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    this.navigate(VISUAL_TRACK_SCREEN_ROUTE, navOptions, navigatorExtras)
}

fun NavGraphBuilder.visualTrackScreen(
    onBackClick: () -> Unit,
) {
    composable(route = VISUAL_TRACK_SCREEN_ROUTE) {
        visualTrackRouter(onBackClick)
    }
}