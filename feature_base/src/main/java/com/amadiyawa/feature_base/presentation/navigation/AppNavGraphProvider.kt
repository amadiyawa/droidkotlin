package com.amadiyawa.feature_base.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

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
     */
    val isMainStartDestination: Boolean
}