package com.amadiyawa.feature_users.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.People
import com.amadiyawa.feature_base.presentation.navigation.DestinationPlacement
import com.amadiyawa.feature_base.presentation.navigation.NavDestinationContract
import com.amadiyawa.feature_user.R

/**
 * Represents the navigation destination for the "Users" feature.
 * Implements the [NavDestinationContract] to define the route, destination, title, icons,
 * and placement for the "Users" screen in the navigation system.
 */
object UserDestination : NavDestinationContract {
    /**
     * The route used to navigate to the "Users" screen.
     */
    override val route = UserListNavigation.route

    /**
     * The unique destination identifier for the "Users" screen.
     */
    override val destination = UserListNavigation.destination

    /**
     * The resource ID for the title of the "Users" screen.
     */
    override val title = R.string.users

    /**
     * The icon displayed when the "Users" screen is selected.
     */
    override val selectedIcon = Icons.Filled.People

    /**
     * The icon displayed when the "Users" screen is not selected.
     */
    override val unselectedIcon = Icons.Outlined.People

    /**
     * Specifies that the "Users" screen should be placed in the BottomBar navigation.
     */
    override val placement = DestinationPlacement.BottomBar
}