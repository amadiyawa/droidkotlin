package com.amadiyawa.feature_onboarding.presentation.screen.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amadiyawa.feature_onboarding.presentation.screen.onboardlist.OnboardListScreen

/**
 * Adds the onboarding list navigation graph to the [NavGraphBuilder].
 *
 * This function defines a composable destination for the onboarding list screen
 * and associates it with the specified route. When the screen is finished, the
 * provided [onFinished] callback is invoked.
 *
 * @param onFinished A lambda function to be executed when the onboarding process is completed.
 */
fun NavGraphBuilder.onboardListGraph(onFinished: () -> Unit) {
    composable(route = OnboardListNavigation.route) {
        OnboardListScreen(onFinished = onFinished)
    }
}