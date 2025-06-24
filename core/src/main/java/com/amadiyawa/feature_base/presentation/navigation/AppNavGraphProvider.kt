package com.amadiyawa.feature_base.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.amadiyawa.feature_base.domain.util.UserRole

/**
 * Defines the contract for providing navigation graphs in the application.
 *
 * This interface is responsible for building navigation graphs using a [NavGraphBuilder]
 * and managing navigation-related properties such as the start destination and whether
 * it is the main start destination of the application.
 */
interface AppNavGraphProvider {
    /**
     * Builds the navigation graph for a specific feature or module.
     *
     * @param navController The [NavHostController] used to manage navigation actions.
     */
    fun NavGraphBuilder.build(navController: NavHostController)

    /**
     * The start destination route for the navigation graph.
     */
    val startDestination: String

    /**
     * Indicates whether this navigation graph is the main start destination of the application.
     * Each role should have exactly one main start destination.
     */
    val isMainStartDestination: Boolean

    /**
     * The roles allowed to access this navigation graph.
     * If empty, all roles have access.
     */
    val allowedRoles: Set<UserRole> get() = setOf(UserRole.NONE, UserRole.ADMIN)
}