package com.amadiyawa.feature_base.presentation.navigation

/**
 * `AppNavigationDestination` is an interface that represents a navigation destination in the application.
 * Each feature module in the application should define an object that implements this interface to provide
 * information about its navigation routes.
 *
 * @property route The main route for the feature. This is the route that will be used as the start destination
 *                 when navigating to the feature.
 * @property destination The name of the destination. This is used to identify the feature in navigation graphs
 *                       and logs.
 */
interface AppNavigationDestination {
    val route: String
    val destination: String
}