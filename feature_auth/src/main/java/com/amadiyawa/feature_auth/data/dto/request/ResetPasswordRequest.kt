package com.amadiyawa.feature_auth.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request object for password reset operation.
 *
 * This data class represents the request payload sent to the API
 * when a user wants to reset their password after successfully verifying their OTP.
 *
 * The flow is: ForgotPassword → OTP Verification → ResetPassword
 *
 * @property resetToken The token received after successful OTP verification (not the OTP itself)
 * @property newPassword The new password to be set
 * @property confirmPassword Confirmation of the new password (optional - validation can be client-side)
 */
@Serializable
data class ResetPasswordRequest(

    @SerialName("resetToken")
    val resetToken: String,

    @SerialName("newPassword")
    val newPassword: String,

    @SerialName("confirmPassword")
    val confirmPassword: String? = null
)