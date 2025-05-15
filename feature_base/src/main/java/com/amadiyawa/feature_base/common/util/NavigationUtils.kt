package com.amadiyawa.feature_base.common.util

import com.amadiyawa.feature_base.domain.model.UserSessionManager
import com.amadiyawa.feature_base.presentation.navigation.AppNavGraphProvider
import timber.log.Timber

/**
 * Determines the main start destination based on the current user's role and available graph providers.
 *
 * @param graphProviders A collection of all available navigation graph providers.
 * @param userSessionManager The user session manager to get the current role.
 * @return The start destination route for the main navigation graph.
 */
fun getMainStartDestination(
    graphProviders: Collection<AppNavGraphProvider>,
    userSessionManager: UserSessionManager
): String {
    // Get current user role
    val currentRole = userSessionManager.currentRole.value
    Timber.d("Current user role: $currentRole")

    // Log all providers
    Timber.d("All graph providers (${graphProviders.size}):")
    graphProviders.forEach { provider ->
        Timber.d("Provider - route: ${provider.startDestination}, isMain: ${provider.isMainStartDestination}, roles: ${provider.allowedRoles}")
    }

    // Filter graph providers based on user role
    val accessibleProviders = graphProviders.filter { provider ->
        val hasAccess = provider.allowedRoles.contains(currentRole)
        Timber.d("Provider ${provider.startDestination} has access: $hasAccess")
        hasAccess
    }
    Timber.d("Accessible providers: ${accessibleProviders.size}")

    // Find providers marked as main
    val mainProviders = accessibleProviders.filter { it.isMainStartDestination }
    Timber.d("Main providers: ${mainProviders.size}")
    mainProviders.forEach {
        Timber.d("Main provider: ${it.startDestination}")
    }

    // If multiple main providers, prioritize based on route order
    val startDestination = if (mainProviders.isNotEmpty()) {
        // Sort by order that matches the UI (invoice first, then users)
        val routeOrder = mapOf(
            "invoice_list" to 1,
            "user_list" to 2
            // Add other routes as needed
        )

        val selected = mainProviders.minByOrNull {
            routeOrder[it.startDestination] ?: Int.MAX_VALUE
        }?.startDestination ?: accessibleProviders.firstOrNull()?.startDestination ?: ""

        Timber.d("Selected main provider: $selected")
        selected
    } else {
        // If no main providers, use first accessible provider
        val selected = accessibleProviders.firstOrNull()?.startDestination ?: ""
        Timber.d("No main providers, using first accessible: $selected")
        selected
    }

    Timber.d("Final start destination: $startDestination")
    return startDestination
}