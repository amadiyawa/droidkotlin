package com.amadiyawa.feature_auth.presentation.screen.signin

import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

/**
 * Represents the UI state for the Sign-In screen.
 *
 * This sealed class defines the various states that the Sign-In screen can be in.
 * It extends the `BaseState` interface to integrate with the application's state management.
 */
sealed class SignInUiState : BaseState {
    data class Idle(val form: SignInForm = SignInForm()) : SignInUiState()

    sealed class Loading(open val form: SignInForm) : SignInUiState() {
        data class Authentication(override val form: SignInForm) : Loading(form)
        data class SessionSaving(override val form: SignInForm) : Loading(form)
        data class SessionActivation(override val form: SignInForm) : Loading(form)
        data class SocialAuthentication(
            override val form: SignInForm,
            val provider: SocialProvider
        ) : Loading(form)
    }

    data class Error(val form: SignInForm, val message: String) : SignInUiState()
}