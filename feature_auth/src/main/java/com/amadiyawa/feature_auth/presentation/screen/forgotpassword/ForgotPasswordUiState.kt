package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import com.amadiyawa.feature_auth.domain.model.ForgotPasswordForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

sealed class ForgotPasswordUiState : BaseState {
    data class Idle(val form: ForgotPasswordForm = ForgotPasswordForm()) : ForgotPasswordUiState()
    data class Loading(val form: ForgotPasswordForm) : ForgotPasswordUiState()
    data class Error(val form: ForgotPasswordForm, val message: String) : ForgotPasswordUiState()
}