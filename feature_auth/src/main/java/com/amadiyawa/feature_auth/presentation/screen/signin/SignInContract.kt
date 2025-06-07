package com.amadiyawa.feature_auth.presentation.screen.signin

import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

/**
 * Contract class for SignInScreen that defines the UI state and user actions.
 *
 * This contract follows your existing MVI pattern with State and Action only.
 * Events are handled separately through your existing UI event system.
 */
class SignInContract {

    /**
     * Represents the UI state of the Sign-In screen.
     *
     * This sealed class defines all possible states that the Sign-In screen can be in,
     * following your existing state pattern.
     */
    sealed class State : BaseState {
        abstract val form: SignInForm

        /**
         * Initial idle state when the screen is first loaded.
         *
         * @param form The sign-in form data
         * @param defaultIdentifier Pre-filled identifier (e.g., from password reset flow)
         */
        data class Idle(
            override val form: SignInForm = SignInForm(),
            val defaultIdentifier: String = ""
        ) : State()

        /**
         * Loading states for different sign-in operations.
         */
        sealed class Loading(override val form: SignInForm) : State() {
            /**
             * Loading state during credential authentication.
             */
            data class Authentication(override val form: SignInForm) : Loading(form)

            /**
             * Loading state while saving user session.
             */
            data class SessionSaving(override val form: SignInForm) : Loading(form)

            /**
             * Loading state during session activation.
             */
            data class SessionActivation(override val form: SignInForm) : Loading(form)

            /**
             * Loading state for social authentication (Google, Facebook, etc.).
             *
             * @param provider The social provider being used for authentication
             */
            data class SocialAuthentication(
                override val form: SignInForm,
                val provider: SocialProvider
            ) : Loading(form)
        }

        /**
         * Error state when sign-in operations fail.
         *
         * @param form The current form state
         * @param message Error message to display
         * @param canRetry Whether the user can retry the operation
         */
        data class Error(
            override val form: SignInForm,
            val message: String,
            val canRetry: Boolean = true
        ) : State()

        /**
         * Success state when sign-in is completed (transitional state).
         *
         * @param form The final form state
         */
        data class Success(override val form: SignInForm) : State()
    }

    /**
     * Actions that can be dispatched to the ViewModel.
     *
     * These represent all possible user interactions and system events
     * that can occur on the Sign-In screen. No need to extend BaseAction
     * since your current implementation doesn't use it.
     */
    sealed class Action {
        /**
         * Update a specific form field.
         *
         * @param field The field to update ("identifier" or "password")
         * @param value The new field value
         */
        data class UpdateField(val field: String, val value: FieldValue) : Action()

        /**
         * Batch update multiple form fields.
         *
         * @param updates Map of field names to their new values
         */
        data class UpdateFields(val updates: Map<String, String>) : Action()

        /**
         * Toggle password visibility (show/hide).
         */
        data object TogglePasswordVisibility : Action()

        /**
         * Toggle remember me checkbox.
         */
        data object ToggleRememberMe : Action()

        /**
         * Submit the sign-in form.
         */
        data object Submit : Action()

        /**
         * Retry the last failed operation.
         */
        data object Retry : Action()

        /**
         * Navigate to forgot password screen.
         */
        data object ForgotPassword : Action()

        /**
         * Initiate social sign-in with specified provider.
         *
         * @param provider The social provider (Google, Facebook, etc.)
         */
        data class SocialSignIn(val provider: SocialProvider) : Action()

        /**
         * Clear all error states and messages.
         */
        data object ClearErrors : Action()

        /**
         * Initialize the screen with pre-filled data.
         *
         * @param defaultIdentifier Pre-filled identifier (from navigation args)
         */
        data class Initialize(val defaultIdentifier: String = "") : Action()

        /**
         * Navigate back to previous screen.
         */
        data object NavigateBack : Action()

        /**
         * Navigate to sign-up screen.
         */
        data object NavigateToSignUp : Action()
    }

    /**
     * Events that can be emitted by the ViewModel (one-time effects).
     *
     * These represent navigation events, snackbar messages, and other
     * one-time effects that should not be part of the persistent UI state.
     */
    sealed class Event {
        /**
         * Navigate to the main screen after successful sign-in.
         */
        data object NavigateToMainScreen : Event()

        /**
         * Navigate to the forgot password screen.
         */
        data object NavigateToForgotPassword : Event()

        /**
         * Navigate to the sign-up screen.
         */
        data object NavigateToSignUp : Event()

        /**
         * Navigate back to the previous screen.
         */
        data object NavigateBack : Event()

        /**
         * Show a snackbar message.
         *
         * @param message The snackbar message text
         * @param isError Whether this is an error message
         */
        data class ShowSnackbar(
            val message: String,
            val isError: Boolean = false
        ) : Event()

        /**
         * Handle social sign-in result.
         *
         * @param provider The social provider used
         * @param success Whether the social sign-in was successful
         * @param message Optional message (error or success)
         */
        data class SocialSignInResult(
            val provider: SocialProvider,
            val success: Boolean,
            val message: String? = null
        ) : Event()

        /**
         * Request focus on a specific field.
         *
         * @param field The field to focus ("identifier" or "password")
         */
        data class RequestFocus(val field: String) : Event()

        /**
         * Hide keyboard.
         */
        data object HideKeyboard : Event()

        /**
         * Show keyboard.
         */
        data object ShowKeyboard : Event()
    }
}