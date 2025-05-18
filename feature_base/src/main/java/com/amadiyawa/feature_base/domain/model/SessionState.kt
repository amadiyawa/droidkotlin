package com.amadiyawa.feature_base.domain.model

/**
 * Represents the current authentication state of the user session.
 * This is observed by all features to react to authentication changes.
 */
sealed class SessionState {
    /**
     * User is not authenticated
     */
    object NotAuthenticated : SessionState()

    /**
     * User is authenticated with user data available
     * @param user The authenticated user's data
     */
    data class Authenticated(val user: UserData) : SessionState()

    /**
     * Authentication state is being determined (app startup, token refresh, etc.)
     */
    object Loading : SessionState()

    /**
     * Authentication is in progress (sign-in, sign-up)
     */
    object Authenticating : SessionState()
}

// Extension functions for convenience
val SessionState.isAuthenticated: Boolean
    get() = this is SessionState.Authenticated

val SessionState.user: UserData?
    get() = (this as? SessionState.Authenticated)?.user

val SessionState.isLoading: Boolean
    get() = this is SessionState.Loading || this is SessionState.Authenticating