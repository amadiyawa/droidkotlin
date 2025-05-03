package com.amadiyawa.feature_auth.presentation.screen.otpverification

import com.amadiyawa.feature_auth.domain.model.SignUp

internal sealed interface OtpUiEvent {

    data class NavigateToNextScreen(val signUp: SignUp) : OtpUiEvent
}