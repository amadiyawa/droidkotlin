package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import com.amadiyawa.feature_auth.domain.model.VerificationResult

internal sealed interface ForgotPasswordUiEvent {
    data class NavigateToOtp(val data: VerificationResult) : ForgotPasswordUiEvent
    data class ShowSnackbar(val message: String) : ForgotPasswordUiEvent
}