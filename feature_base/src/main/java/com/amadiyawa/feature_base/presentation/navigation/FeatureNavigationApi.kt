package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.amadiyawa.feature_base.domain.util.UserRole

/**
 * Base interface for all feature navigation providers.
 * Ensures proper identification and classification of navigation destinations.
 */
interface FeatureNavigationApi {
    /**
     * Unique identifier for this feature
     */
    val featureId: String

    /**
     * Builds the navigation graph for this feature
     */
    fun NavGraphBuilder.registerNavigation(navController: NavHostController)

    /**
     * User roles that can access this feature
     */
    val allowedRoles: Set<UserRole>

    /**
     * Whether this feature should be the main destination for allowed roles
     */
    val isMainDestination: Boolean

    /**
     * Returns a list of navigation destinations to be displayed in navigation UI elements
     */
    fun getNavigationDestinations(): List<NavigationDestination>
}

/**
 * Represents a UI destination in the navigation system
 */
data class NavigationDestination(
    val route: String,
    val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val placement: DestinationPlacement,
    val order: Int,
    val deepLinks: List<String> = emptyList(),
    val badge: BadgeInfo? = null
)

/**
 * Badge information for a navigation destination
 */
data class BadgeInfo(
    val count: Int,
    val showAsNumber: Boolean = true
)

/**
 * Placement of a navigation destination in the UI
 */
enum class DestinationPlacement {
    BottomBar,
    NavRail,
    Drawer,
    Hidden
}