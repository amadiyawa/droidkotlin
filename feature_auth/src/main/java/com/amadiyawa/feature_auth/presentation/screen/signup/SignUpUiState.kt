package com.amadiyawa.feature_auth.presentation.screen.signup

import androidx.compose.runtime.Immutable
import com.amadiyawa.feature_auth.domain.model.SignUpForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

@Immutable
sealed interface SignUpUiState : BaseState {

    data class Idle(
        val form: SignUpForm = SignUpForm()
    ) : SignUpUiState

    data class Loading(
        val form: SignUpForm
    ) : SignUpUiState

    data class Error(
        val form: SignUpForm,
        val message: String
    ) : SignUpUiState
}
