package com.amadiyawa.feature_auth.presentation.screen.signin

import com.amadiyawa.feature_auth.domain.enum.SocialProvider
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

sealed class SignInUiState : BaseState {
    data class Idle(val form: SignInForm = SignInForm()) : SignInUiState()

    sealed class Loading(open val form: SignInForm) : SignInUiState() {
        data class Authentication(override val form: SignInForm) : Loading(form)
        data class SessionSaving(override val form: SignInForm) : Loading(form)
        data class SessionActivation(override val form: SignInForm) : Loading(form)
        data class SocialAuthentication(
            override val form: SignInForm,
            val provider: SocialProvider
        ) : Loading(form)
    }

    data class Error(val form: SignInForm, val message: String) : SignInUiState()
}