package com.amadiyawa.feature_auth.presentation.screen.resetpassword

import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseAction

sealed interface ResetPasswordAction : BaseAction {
    data class UpdateField(val key: String, val value: FieldValue) : ResetPasswordAction
    data object Submit : ResetPasswordAction
    data class ShowError(val message: String) : ResetPasswordAction
}