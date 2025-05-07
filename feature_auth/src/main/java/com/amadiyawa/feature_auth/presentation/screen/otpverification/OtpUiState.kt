package com.amadiyawa.feature_auth.presentation.screen.otpverification

import androidx.compose.runtime.Immutable
import com.amadiyawa.feature_auth.domain.model.OtpForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

@Immutable
sealed interface OtpUiState : BaseState {
    val form: OtpForm
    val errorMessage: String?

    data class Idle(
        override val form: OtpForm,
        override val errorMessage: String? = null
    ) : OtpUiState

    data class Loading(
        override val form: OtpForm,
        override val errorMessage: String? = null,
        val isResending: Boolean = false
    ) : OtpUiState

    data class Error(
        override val form: OtpForm,
        override val errorMessage: String,
        val isFatal: Boolean = false
    ) : OtpUiState
}

val OtpUiState.isLoading: Boolean
    get() = this is OtpUiState.Loading