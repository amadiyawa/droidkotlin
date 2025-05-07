package com.amadiyawa.feature_auth.presentation.screen.otpverification

import com.amadiyawa.feature_auth.domain.model.OtpVerificationResult
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseAction

internal sealed interface OtpAction : BaseAction {
    // User Interactions
    data class UpdateDigit(val index: Int, val value: String) : OtpAction
    data object Submit : OtpAction
    data object ResendOtp : OtpAction

    // System Events
    data class Initialize(val verificationId: String, val purpose: String, val recipient: String) : OtpAction
    data class HandleVerificationSuccess(val result: OtpVerificationResult) : OtpAction
    data class HandleVerificationError(val error: OperationResult.Failure) : OtpAction
    data object StartCountdown : OtpAction
    data class CountdownTick(val secondsRemaining: Int) : OtpAction
}