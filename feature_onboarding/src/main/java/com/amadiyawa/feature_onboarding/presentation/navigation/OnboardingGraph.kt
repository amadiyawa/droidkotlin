package com.amadiyawa.feature_onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.amadiyawa.feature_base.presentation.navigation.AppRoutes
import com.amadiyawa.feature_onboarding.presentation.screen.onboarding.OnboardingScreen
import timber.log.Timber

/**
 * Adds the onboarding navigation graph to the [NavGraphBuilder].
 *
 * This function defines the onboarding navigation graph and handles the completion
 * internally by navigating to the auth graph.
 *
 * @param navController The NavHostController to handle navigation
 */
fun NavGraphBuilder.onboardingGraph(navController: NavHostController) {
    navigation(
        startDestination = OnboardingRoutes.ONBOARDING,
        route = AppRoutes.ONBOARDING_GRAPH
    ) {
        composable(OnboardingRoutes.ONBOARDING) {
            Timber.d("Navigating to OnboardingScreen")
            OnboardingScreen(
                onFinished = {
                    navController.finishOnboarding()
                }
            )
        }
    }
}