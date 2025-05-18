package com.amadiyawa.feature_auth.presentation.navigation

import androidx.navigation.NavController
import com.amadiyawa.feature_base.presentation.navigation.AppRoutes

/**
 * Navigates to the main graph in the application.
 *
 * This function is used to navigate from the authentication graph to the main graph.
 * It clears the back stack up to the authentication graph to ensure that the user
 * cannot navigate back to the authentication screens after successfully logging in.
 *
 * The navigation is launched as a single top instance to avoid creating multiple
 * instances of the main graph.
 */
internal fun NavController.navigateToMain() {
    navigate(AppRoutes.MAIN_GRAPH) {
        popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
        launchSingleTop = true
    }
}