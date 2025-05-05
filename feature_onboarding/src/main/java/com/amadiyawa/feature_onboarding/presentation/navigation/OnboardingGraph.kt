package com.amadiyawa.feature_onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amadiyawa.feature_onboarding.presentation.screen.onboarding.OnboardingScreen

/**
 * Adds the onboarding navigation graph to the [NavGraphBuilder].
 *
 * This function defines a composable destination for the onboarding list screen
 * and associates it with the specified route. When the screen is finished, the
 * provided [onFinished] callback is invoked.
 *
 * @param onFinished A lambda function to be executed when the onboarding process is completed.
 */
fun NavGraphBuilder.onboardingGraph(onFinished: () -> Unit) {
    composable(route = OnboardingNavigation.route) {
        OnboardingScreen(onFinished = onFinished)
    }
}