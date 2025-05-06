package com.amadiyawa.feature_auth.presentation.screen.otpverification

import com.amadiyawa.feature_auth.domain.model.VerificationResult

internal sealed interface OtpUiEvent {

    data class NavigateToNextScreen(val data: VerificationResult) : OtpUiEvent
}