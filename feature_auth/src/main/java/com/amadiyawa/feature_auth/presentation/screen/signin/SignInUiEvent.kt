package com.amadiyawa.feature_auth.presentation.screen.signin

sealed interface SignInUiEvent {
    data object NavigateToMainScreen : SignInUiEvent
    data object NavigateToForgotPassword : SignInUiEvent
    data class ShowSnackbar(val message: String) : SignInUiEvent
}