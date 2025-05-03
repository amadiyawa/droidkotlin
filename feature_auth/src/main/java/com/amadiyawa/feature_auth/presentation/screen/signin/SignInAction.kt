package com.amadiyawa.feature_auth.presentation.screen.signin

import com.amadiyawa.feature_auth.domain.enum.SocialProvider
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseAction

sealed interface SignInAction : BaseAction {
    data class UpdateField(val key: String, val value: FieldValue) : SignInAction
    data object TogglePasswordVisibility : SignInAction
    data object Submit : SignInAction
    data object ForgotPassword : SignInAction
    data class SocialSignIn(val provider: SocialProvider) : SignInAction
    data object ClearErrors : SignInAction
}