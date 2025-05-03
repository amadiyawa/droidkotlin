package com.amadiyawa.feature_auth.presentation.screen.resetpassword

import com.amadiyawa.feature_auth.domain.model.ResetPasswordForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

sealed class ResetPasswordUiState : BaseState {
    data class Idle(val form: ResetPasswordForm = ResetPasswordForm()) : ResetPasswordUiState()
    data class Loading(val form: ResetPasswordForm) : ResetPasswordUiState()
    data class Error(val form: ResetPasswordForm, val message: String) : ResetPasswordUiState()
}