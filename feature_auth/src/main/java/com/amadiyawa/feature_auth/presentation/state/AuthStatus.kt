package com.amadiyawa.feature_auth.presentation.state

/**
 * Represents the authentication status in the application.
 *
 * This sealed interface defines the various states that the authentication process
 * can be in, such as idle, loading, authenticated, invalid, or error.
 * Each state is represented as an object or a data class.
 */
sealed interface AuthStatus {
    /**
     * Represents the idle state where no authentication process is ongoing.
     */
    object Idle : AuthStatus

    /**
     * Represents the loading state where an authentication process is in progress.
     */
    object Loading : AuthStatus

    /**
     * Represents the state where the user is successfully authenticated.
     */
    object Authenticated : AuthStatus

    /**
     * Represents the state where the authentication is invalid.
     *
     * @property message An optional message providing details about the invalid state.
     */
    data class Invalid(val message: String? = null) : AuthStatus

    /**
     * Represents the state where an error occurred during authentication.
     *
     * @property throwable An optional throwable providing details about the error.
     */
    data class Error(val throwable: Throwable? = null) : AuthStatus
}