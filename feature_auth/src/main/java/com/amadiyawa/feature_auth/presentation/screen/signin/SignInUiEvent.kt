package com.amadiyawa.feature_auth.presentation.screen.signin

import com.amadiyawa.feature_auth.domain.enum.SocialProvider
import com.amadiyawa.feature_base.presentation.common.SnackbarMessage

sealed interface SignInUiEvent {
    data object NavigateToMainScreen : SignInUiEvent
    data object NavigateToForgotPassword : SignInUiEvent
    data class SocialSignInResult(
        val provider: SocialProvider,
        val success: Boolean,
        val message: String? = null
    ) : SignInUiEvent
    data class ShowSnackbar(
        val snackbarMessage: SnackbarMessage
    ) : SignInUiEvent {
        constructor(
            message: String,
            isError: Boolean = false
        ) : this(SnackbarMessage(message, isError))
    }
}