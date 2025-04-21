package com.amadiyawa.feature_auth.presentation.screen.signin

import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

sealed class SignInUiState : BaseState {
    data class Idle(val form: SignInForm = SignInForm()) : SignInUiState()
    data class Loading(val form: SignInForm) : SignInUiState()
    data class Error(val form: SignInForm, val message: String) : SignInUiState()
}