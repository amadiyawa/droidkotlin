package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.amadiyawa.feature_base.domain.util.UserRole

/**
 * Interface that defines the contract for navigation destinations in the application.
 */
interface NavDestinationContract {
    /**
     * The route used for navigation.
     */
    val route: String

    /**
     * The destination identifier.
     */
    val destination: String

    /**
     * The resource ID for the title.
     */
    val title: Int

    /**
     * The icon used when this destination is selected.
     */
    val selectedIcon: ImageVector

    /**
     * The icon used when this destination is not selected.
     */
    val unselectedIcon: ImageVector

    /**
     * The placement of this destination in the UI (BottomBar, NavRail, etc.).
     */
    val placement: DestinationPlacement

    /**
     * The roles that have access to this destination.
     * If empty, all roles have access.
     */
    val allowedRoles: Set<UserRole> get() = setOf(UserRole.NONE, UserRole.ADMIN)

    // Optional: Add order property with default implementation
    val order: Int get() = Int.MAX_VALUE
}