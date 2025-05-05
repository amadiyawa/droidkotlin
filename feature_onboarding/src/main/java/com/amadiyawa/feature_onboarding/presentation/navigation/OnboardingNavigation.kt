package com.amadiyawa.feature_onboarding.presentation.navigation

import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination

/**
 * Represents the navigation configuration for the Onboarding feature.
 *
 * This object defines the navigation route and destination for the onboarding list screen.
 * It implements the [AppNavigationDestination] interface to provide the required
 * navigation properties.
 */
object OnboardingNavigation: AppNavigationDestination {
    override val route = "onboarding"

    // The destination for the User feature
    override val destination = "onboarding_destination"
}