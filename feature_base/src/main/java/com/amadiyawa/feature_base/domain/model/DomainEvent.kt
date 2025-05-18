package com.amadiyawa.feature_base.domain.model

/**
 * Domain events for cross-feature communication.
 * These events represent significant business events that multiple features might care about.
 */
sealed class DomainEvent {
    /**
     * User has successfully signed in
     * @param userData The authenticated user's data
     * @param isFirstTimeUser Whether this is the user's first sign-in
     */
    data class UserSignedIn(
        val userData: UserData,
        val isFirstTimeUser: Boolean = false
    ) : DomainEvent()

    /**
     * User profile has been updated
     * @param userData The updated user data
     * @param updatedFields List of fields that were updated
     */
    data class UserProfileUpdated(
        val userData: UserData,
        val updatedFields: List<String> = emptyList()
    ) : DomainEvent()

    /**
     * User has signed out
     * @param reason Optional reason for sign out (user initiated, token expired, etc.)
     * @param userId The ID of the user who signed out
     */
    data class UserSignedOut(
        val reason: String? = null,
        val userId: String? = null
    ) : DomainEvent()

    /**
     * User avatar has been updated
     * @param avatarUrl The new avatar URL
     * @param userId The ID of the user whose avatar was updated
     */
    data class UserAvatarUpdated(
        val avatarUrl: String,
        val userId: String
    ) : DomainEvent()

    /**
     * User preferences have been updated
     * @param preferences Map of updated preferences
     * @param userId The ID of the user whose preferences were updated
     */
    data class UserPreferencesUpdated(
        val preferences: Map<String, Any>,
        val userId: String
    ) : DomainEvent()

    /**
     * Authentication tokens have been refreshed
     * @param userId The ID of the user whose tokens were refreshed
     */
    data class TokensRefreshed(
        val userId: String
    ) : DomainEvent()
}

// Extension functions for type checking
inline fun <reified T : DomainEvent> DomainEvent.isType(): Boolean = this is T

inline fun <reified T : DomainEvent> DomainEvent.asType(): T? = this as? T