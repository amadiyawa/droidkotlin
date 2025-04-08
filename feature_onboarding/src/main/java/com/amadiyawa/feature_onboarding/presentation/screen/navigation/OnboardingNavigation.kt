package com.amadiyawa.feature_onboarding.presentation.screen.navigation

import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination

/**
 * Represents the navigation configuration for the Onboarding feature.
 *
 * This object defines the navigation route and destination for the onboarding list screen.
 * It implements the [AppNavigationDestination] interface to provide the required
 * navigation properties.
 */
object OnboardListNavigation: AppNavigationDestination {
    override val route = "onboard_list"

    // The destination for the User feature
    override val destination = "onboard_list_destination"
}