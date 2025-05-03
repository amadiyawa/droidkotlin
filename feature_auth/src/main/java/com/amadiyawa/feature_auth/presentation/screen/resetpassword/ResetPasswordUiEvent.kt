package com.amadiyawa.feature_auth.presentation.screen.resetpassword

sealed interface ResetPasswordUiEvent {
    data class NavigateToSignIn(val identifier: String) : ResetPasswordUiEvent
    data class ShowSnackbar(val message: String) : ResetPasswordUiEvent
}