package com.amadiyawa.feature_auth.presentation.screen.otpverification

sealed interface OtpUiEvent {

    object NavigateToNextScreen : OtpUiEvent
}