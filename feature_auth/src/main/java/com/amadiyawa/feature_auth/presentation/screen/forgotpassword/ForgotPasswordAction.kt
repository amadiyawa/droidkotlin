package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseAction

sealed interface ForgotPasswordAction : BaseAction {
    data class UpdateField(val key: String, val value: FieldValue) : ForgotPasswordAction
    data object Submit : ForgotPasswordAction
    data class ShowError(val message: String) : ForgotPasswordAction
}