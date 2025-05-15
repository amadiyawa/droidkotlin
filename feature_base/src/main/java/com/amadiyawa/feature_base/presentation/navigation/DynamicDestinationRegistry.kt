package com.amadiyawa.feature_base.presentation.navigation

import com.amadiyawa.feature_base.domain.model.UserSessionManager
import com.amadiyawa.feature_base.domain.util.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber

/**
 * A registry for managing dynamic navigation destinations in the application.
 * This object allows for the registration and retrieval of navigation destinations
 * that implement the [NavDestinationContract].
 */
/**
 * A registry for managing dynamic navigation destinations in the application.
 */
class DynamicDestinationRegistry(
    private val userSessionManager: UserSessionManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * A mutable list that holds all registered navigation destinations.
     */
    private val _allDestinations = mutableListOf<NavDestinationContract>()

    /**
     * A mutable state flow that holds the filtered destinations based on user role.
     */
    private val _visibleDestinations = MutableStateFlow<List<NavDestinationContract>>(emptyList())

    /**
     * A public, immutable state flow of the visible destinations for the current user.
     */
    val destinations: StateFlow<List<NavDestinationContract>> = _visibleDestinations.asStateFlow()

    init {
        Timber.d("NewDynamicDestinationRegistry initialized")

        // Observe user role changes
        userSessionManager.currentRole
            .onEach { role ->
                Timber.d("Role changed to: $role, updating visible destinations")
                updateVisibleDestinations(role)
            }
            .launchIn(scope)
    }

    /**
     * Registers a new navigation destination.
     *
     * @param destination The navigation destination to be added to the registry.
     */
    fun register(destination: NavDestinationContract) {
        Timber.d("Registering destination: ${destination.route}, placement: ${destination.placement}")
        _allDestinations.add(destination)
        updateVisibleDestinations(userSessionManager.currentRole.value)
    }

    /**
     * Updates the list of visible destinations based on the user's role.
     */
    private fun updateVisibleDestinations(userRole: UserRole?) {
        Timber.d("Updating visible destinations for role: $userRole, total registered: ${_allDestinations.size}")

        _visibleDestinations.update { _ ->
            if (userRole == null) {
                Timber.d("No user role, returning empty list")
                emptyList()
            } else {
                // Filter destinations based on user role
                val filtered = _allDestinations.filter { destination ->
                    destination.allowedRoles.contains(userRole)
                }
                Timber.d("Filtered destinations: ${filtered.size}")
                filtered
            }
        }
    }
}