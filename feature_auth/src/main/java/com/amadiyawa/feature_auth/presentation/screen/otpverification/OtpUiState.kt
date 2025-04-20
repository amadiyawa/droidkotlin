package com.amadiyawa.feature_auth.presentation.screen.otpverification

import androidx.compose.runtime.Immutable
import com.amadiyawa.feature_auth.domain.model.OtpForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

@Immutable
sealed interface OtpUiState : BaseState {
    val form: OtpForm

    data class Idle(override val form: OtpForm) : OtpUiState
    data class Loading(override val form: OtpForm) : OtpUiState
    data class Error(override val form: OtpForm, val message: String) : OtpUiState
}