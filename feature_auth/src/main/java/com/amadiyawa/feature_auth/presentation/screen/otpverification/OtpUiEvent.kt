package com.amadiyawa.feature_auth.presentation.screen.otpverification

import com.amadiyawa.feature_auth.domain.model.AuthResult

internal sealed interface OtpUiEvent {
    data class NavigateToHome(val authResult: AuthResult) : OtpUiEvent
    data class NavigateToResetPassword(val resetToken: String) : OtpUiEvent
    data class ShowError(val message: String, val isFatal: Boolean = false) : OtpUiEvent
    data object ClearError : OtpUiEvent
    data class UpdateResendAvailability(val canResend: Boolean, val countdown: Int?) : OtpUiEvent
}