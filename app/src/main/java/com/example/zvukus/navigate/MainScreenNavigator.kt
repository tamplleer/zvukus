package com.example.zvukus.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import com.example.zvukus.screen.main.mainRouter

const val MAIN_SCREEN_ROUTE = "MAIN_SCREEN_ROUTE"

fun NavController.navigateToMainScreen(
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    this.navigate(MAIN_SCREEN_ROUTE, navOptions, navigatorExtras)
}

fun NavGraphBuilder.mainScreen(
    onBackClick: () -> Unit,
    toVisualTrack : () -> Unit,
) {
    composable(route = MAIN_SCREEN_ROUTE) {
        mainRouter(onBackClick,toVisualTrack)
    }
}