package com.amadiyawa.feature_auth.presentation.screen.signup

import com.amadiyawa.feature_auth.domain.model.VerificationResult

/**
 * Represents one-time UI events that should not be part of the persistent state
 * and can occur on the Sign-Up screen.
 *
 * This sealed interface defines different types of events that can be triggered
 * during user interaction with the Sign-Up screen. Each event corresponds to a specific
 * action or navigation flow.
 */
internal sealed interface SignUpUiEvent {

    /**
     * Event to navigate to the OTP (One-Time Password) screen.
     *
     * @property data The SignUp object containing the necessary data for the OTP screen.
     */
    data class NavigateToOtp(val data: VerificationResult) : SignUpUiEvent

    /**
     * Event to display a snackbar with a message.
     *
     * @param message The message to be displayed in the snackbar.
     */
    data class ShowSnackbar(val message: String) : SignUpUiEvent
}