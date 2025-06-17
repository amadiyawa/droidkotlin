package com.amadiyawa.feature_auth.presentation.screen.resetpassword

import com.amadiyawa.feature_auth.domain.model.ResetPasswordForm
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

/**
 * Contract class for ResetPasswordScreen that defines the UI state and user actions.
 *
 * This contract follows your existing MVI pattern with State, Action, and Event
 * for the Reset Password feature with enhanced validation and user experience.
 */
class ResetPasswordContract {

    /**
     * Represents the UI state of the Reset Password screen.
     *
     * This sealed class defines all possible states that the Reset Password screen can be in,
     * following your existing state pattern.
     */
    sealed class State : BaseState {
        abstract val form: ResetPasswordForm

        /**
         * Initial idle state when the screen is first loaded.
         *
         * @param form The reset password form data
         * @param resetToken The token for password reset verification
         */
        data class Idle(
            override val form: ResetPasswordForm = ResetPasswordForm(),
            val resetToken: String = ""
        ) : State()

        /**
         * Loading states for different reset password operations.
         */
        sealed class Loading(override val form: ResetPasswordForm) : State() {
            /**
             * Loading state during password reset submission.
             */
            data class ResettingPassword(override val form: ResetPasswordForm) : Loading(form)

            /**
             * Loading state while validating reset token.
             */
            data class ValidatingToken(override val form: ResetPasswordForm) : Loading(form)
        }

        /**
         * Error state when reset password operations fail.
         *
         * @param form The current form state
         * @param message Error message to display
         * @param canRetry Whether the user can retry the operation
         */
        data class Error(
            override val form: ResetPasswordForm,
            val message: String,
            val canRetry: Boolean = true
        ) : State()

        /**
         * Success state when password reset is completed (transitional state).
         *
         * @param form The final form state
         * @param recipient The identifier to pre-fill in sign-in (email/phone/username)
         */
        data class Success(
            override val form: ResetPasswordForm,
            val recipient: String
        ) : State()
    }

    /**
     * Actions that can be dispatched to the ViewModel.
     *
     * These represent all possible user interactions and system events
     * that can occur on the Reset Password screen.
     */
    sealed class Action {
        /**
         * Update a specific form field.
         *
         * @param field The field to update ("password" or "confirmPassword")
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
         * Toggle confirm password visibility (show/hide).
         */
        data object ToggleConfirmPasswordVisibility : Action()

        /**
         * Submit the password reset form.
         */
        data object Submit : Action()

        /**
         * Retry the last failed operation.
         */
        data object Retry : Action()

        /**
         * Initialize the screen with reset token.
         *
         * @param resetToken The token from URL/deep link
         */
        data class Initialize(val resetToken: String) : Action()

        /**
         * Clear all error states and messages.
         */
        data object ClearErrors : Action()

        /**
         * Navigate back to previous screen.
         */
        data object NavigateBack : Action()

        /**
         * Show error message (internal action).
         *
         * @param message Error message to display
         */
        data class ShowError(val message: String) : Action()
    }

    /**
     * Events that can be emitted by the ViewModel (one-time effects).
     *
     * These represent navigation events, snackbar messages, and other
     * one-time effects that should not be part of the persistent UI state.
     */
    sealed class Event {
        /**
         * Navigate to sign-in screen with pre-filled identifier.
         *
         * @param recipient The identifier to pre-fill (email/phone/username)
         */
        data class NavigateToSignIn(val recipient: String) : Event()

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
         * Show error message with optional retry action.
         *
         * @param message Error message to display
         * @param canRetry Whether retry action should be available
         */
        data class ShowError(
            val message: String,
            val canRetry: Boolean = true
        ) : Event()

        /**
         * Request focus on a specific field.
         *
         * @param field The field to focus ("password" or "confirmPassword")
         */
        data class RequestFocus(val field: String) : Event()

        /**
         * Hide keyboard.
         */
        data object HideKeyboard : Event()

        /**
         * Show password strength indicator.
         *
         * @param show Whether to show the strength indicator
         */
        data class ShowPasswordStrength(val show: Boolean) : Event()
    }
}