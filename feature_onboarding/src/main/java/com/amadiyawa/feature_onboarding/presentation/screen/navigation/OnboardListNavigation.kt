package com.amadiyawa.feature_onboarding.presentation.screen.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination
import com.amadiyawa.feature_onboarding.presentation.screen.onboardlist.OnboardListScreen

/**
 * Defines the navigation route and destination for the Onboarding feature.
 */
object OnboardListNavigation: AppNavigationDestination {
    private const val ONBOARD_LIST = "onboard_list"

    // The route for the Onboarding feature
    override val route = ONBOARD_LIST

    // The destination for the User feature
    override val destination = "${ONBOARD_LIST}_destination"
}

/**
 * This function is used to define
 * the navigation graph for the Onboarding feature.
 */
fun NavGraphBuilder.onboardListGraph() {
    navigation(startDestination = OnboardListNavigation.destination, route = OnboardListNavigation.route) {
        composable(route = OnboardListNavigation.destination) {
            OnboardListScreen()
        }
    }
}