package com.amadiyawa.feature_auth.presentation.screen.otpverification

sealed interface ResendState {
    data object Available : ResendState
    data class Countdown(val secondsRemaining: Int) : ResendState
}