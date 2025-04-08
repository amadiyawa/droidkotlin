package com.amadiyawa.feature_base.common.util

import com.amadiyawa.feature_base.presentation.navigation.AppNavGraphProvider

/**
 * Retrieves the main start destination from a list of navigation graph providers.
 *
 * This function iterates through the provided list of [AppNavGraphProvider] and returns
 * the start destination of the first provider marked as the main start destination.
 * If no such provider is found, an error is thrown.
 *
 * @param graphProviders A list of [AppNavGraphProvider] instances to search through.
 * @return The start destination as a [String] of the main navigation graph.
 * @throws IllegalStateException If no main start destination is found in the list.
 */
fun getMainStartDestination(graphProviders: List<AppNavGraphProvider>): String {
    return graphProviders.firstOrNull { it.isMainStartDestination }?.startDestination
        ?: error("No main start destination found")
}