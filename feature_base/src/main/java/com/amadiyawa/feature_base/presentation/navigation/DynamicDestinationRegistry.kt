package com.amadiyawa.feature_base.presentation.navigation

/**
 * A registry for managing dynamic navigation destinations in the application.
 * This object allows for the registration and retrieval of navigation destinations
 * that implement the [NavDestinationContract].
 */
object DynamicDestinationRegistry {
    /**
     * A mutable list that holds all registered navigation destinations.
     * This list is private to ensure encapsulation and is exposed as an immutable list.
     */
    private val _destinations = mutableListOf<NavDestinationContract>()

    /**
     * A public, immutable list of all registered navigation destinations.
     * Use this property to access the registered destinations.
     */
    val destinations: List<NavDestinationContract> get() = _destinations

    /**
     * Registers a new navigation destination.
     *
     * @param destination The navigation destination to be added to the registry.
     */
    fun register(destination: NavDestinationContract) {
        _destinations.add(destination)
    }
}