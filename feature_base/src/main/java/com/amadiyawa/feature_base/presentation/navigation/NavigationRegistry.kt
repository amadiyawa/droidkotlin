package com.amadiyawa.feature_base.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.amadiyawa.feature_base.domain.util.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

/**
 * Central registry for all feature navigation APIs.
 * Manages registration and filtering of navigation components.
 */
class NavigationRegistry {
    private val _featureApis = mutableListOf<FeatureNavigationApi>()
    private val _visibleDestinations = MutableStateFlow<List<NavigationDestination>>(emptyList())

    val visibleDestinations: StateFlow<List<NavigationDestination>> = _visibleDestinations.asStateFlow()

    /**
     * Registers a feature navigation API
     */
    fun registerFeature(api: FeatureNavigationApi) {
        if (_featureApis.none { it.featureId == api.featureId }) {
            _featureApis.add(api)
            Timber.d("Registered feature: ${api.featureId}")
            updateVisibleDestinations()
        } else {
            Timber.w("Feature already registered: ${api.featureId}")
        }
    }

    /**
     * Returns a list of all registered feature IDs for verification
     */
    fun getRegisteredFeatureIds(): List<String> {
        return _featureApis.map { it.featureId }
    }

    /**
     * Updates the set of visible destinations based on the current user role
     */
    fun updateVisibleDestinations(currentRole: UserRole? = null) {
        _visibleDestinations.update { _ ->
            if (currentRole == null) {
                emptyList()
            } else {
                _featureApis
                    .filter { it.allowedRoles.contains(currentRole) }
                    .flatMap { it.getNavigationDestinations() }
                    .sortedBy { it.order }
            }
        }
    }

    /**
     * Registers all feature navigation graphs with the NavGraphBuilder
     */
    fun NavGraphBuilder.registerFeatureNavigations(
        navController: NavHostController,
        currentRole: UserRole?
    ) {
        _featureApis
            .filter { api -> currentRole == null || api.allowedRoles.contains(currentRole) }
            .forEach { api ->
                Timber.d("Registering navigation for feature: ${api.featureId}")
                with(api) {
                    registerNavigation(navController)
                }
            }
    }

    /**
     * Determines the main start destination based on the current user role
     */
    fun getMainStartDestination(currentRole: UserRole?): String {
        if (currentRole == null) {
            Timber.d("No current role, returning empty string")
            return ""
        }

        Timber.d("Finding main destination for role: $currentRole")

        // Find features accessible to this role
        val accessibleFeatures = _featureApis.filter { api ->
            api.allowedRoles.contains(currentRole)
        }

        Timber.d("Accessible features: ${accessibleFeatures.map { it.featureId }}")

        // Find main destination features
        val mainFeatures = accessibleFeatures.filter { it.isMainDestination }

        Timber.d("Main destination features: ${mainFeatures.map { it.featureId }}")

        // If there's exactly one main feature, use it
        if (mainFeatures.size == 1) {
            val destinations = mainFeatures[0].getNavigationDestinations()
            val mainRoute = destinations.firstOrNull()?.route ?: ""
            Timber.d("Selected single main destination: $mainRoute")
            return mainRoute
        }

        // If there are multiple main features, use predefined priorities
        if (mainFeatures.isNotEmpty()) {
            val priorityOrder = mapOf(
                "invoice" to 1,
                "billing" to 2,
                "user" to 3,
                // Add other features in order of priority
            )

            val prioritizedFeature = mainFeatures.minByOrNull { api ->
                priorityOrder[api.featureId] ?: Int.MAX_VALUE
            }

            if (prioritizedFeature != null) {
                val destinations = prioritizedFeature.getNavigationDestinations()
                val mainRoute = destinations.firstOrNull()?.route ?: ""
                Timber.d("Selected prioritized main destination: $mainRoute")
                return mainRoute
            }
        }

        // If no main features, use the first accessible feature
        if (accessibleFeatures.isNotEmpty()) {
            val destinations = accessibleFeatures[0].getNavigationDestinations()
            val fallbackRoute = destinations.firstOrNull()?.route ?: ""
            Timber.d("Selected fallback destination: $fallbackRoute")
            return fallbackRoute
        }

        Timber.d("No suitable destination found, returning empty string")
        return ""
    }
}