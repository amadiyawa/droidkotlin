package com.amadiyawa.feature_auth.presentation.screen.otpverification

import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseAction

internal sealed interface OtpAction : BaseAction {
    data class UpdateDigit(val index: Int, val value: String) : OtpAction
    data object Submit : OtpAction
    data class ShowError(val message: String) : OtpAction
    data class Initialize(val signUp: SignUp) : OtpAction
}