package com.amadiyawa.feature_onboarding.presentation.navigation

import androidx.navigation.NavController
import com.amadiyawa.feature_base.presentation.navigation.AppRoutes
import timber.log.Timber

/**
 * Navigates to the authentication graph in the application.
 *
 * This function is used to transition from the onboarding graph to the authentication graph.
 * It clears the back stack up to the onboarding graph to ensure that the user cannot navigate
 * back to the onboarding screens after completing the onboarding process.
 *
 * The navigation is launched as a single top instance to avoid creating multiple
 * instances of the authentication graph.
 */
internal fun NavController.navigateToAuth() {
    Timber.d("Completing onboarding, navigating to auth")
    navigate(AppRoutes.AUTH_GRAPH) {
        popUpTo(AppRoutes.ONBOARDING_GRAPH) { inclusive = true }
        launchSingleTop = true
    }
}

/**
 * Completes the onboarding process and navigates to the authentication graph.
 *
 * This function serves as a wrapper around `navigateToAuth()` to provide a clear
 * and concise way to indicate the end of the onboarding flow and transition
 * to the authentication screens.
 */
internal fun NavController.finishOnboarding() {
    navigateToAuth()
}