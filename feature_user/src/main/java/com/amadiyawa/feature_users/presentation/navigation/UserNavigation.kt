package com.amadiyawa.feature_users.presentation.navigation

import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination

/**
 * Represents the navigation configuration for the User List feature.
 *
 * This object defines the navigation route and destination for the user list screen.
 * It also provides a utility function to generate the route for the user detail screen
 * based on a given user ID.
 */
object UserListNavigation : AppNavigationDestination {
    override val route = "user_list"
    override val destination = "user_list_destination"
    fun detailRoute(userId: String) = "user_detail/$userId"
}

/**
 * Represents the navigation configuration for the User Detail feature.
 *
 * This object defines the navigation route and destination for the user detail screen.
 * It uses a placeholder for the user ID in the route to support dynamic navigation.
 */
object UserDetailNavigation : AppNavigationDestination {
    override val route = "user_detail/{userId}"
    override val destination = "user_detail_destination"
}