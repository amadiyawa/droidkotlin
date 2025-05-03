package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import com.amadiyawa.feature_auth.domain.model.SignUp

internal sealed interface ForgotPasswordUiEvent {
    data class NavigateToOtp(val signUp: SignUp) : ForgotPasswordUiEvent
    data class ShowSnackbar(val message: String) : ForgotPasswordUiEvent
}