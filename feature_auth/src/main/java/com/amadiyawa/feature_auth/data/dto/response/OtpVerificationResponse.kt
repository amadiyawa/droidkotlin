package com.amadiyawa.feature_auth.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OtpVerificationResponse(
    // Common fields
    @SerialName("success") val success: Boolean,
    @SerialName("purpose") val purpose: String,
    @SerialName("message") val message: String? = null,

    // For SIGN_UP: Auth tokens and user data
    @SerialName("auth_response") val authResponse: AuthResponse? = null,

    // For PASSWORD_RESET: Token to reset password
    @SerialName("resetToken") val resetToken: String? = null,
) {
    companion object {
        fun randomSuccess(purpose: String): OtpVerificationResponse {
            return if (purpose.uppercase() == "SIGN_UP") {
                OtpVerificationResponse(
                    success = true,
                    purpose = "SIGN_UP",
                    authResponse = AuthResponse.random(),
                    message = "OTP verified successfully"
                )
            } else {
                OtpVerificationResponse(
                    success = true,
                    purpose = "PASSWORD_RESET",
                    resetToken = "reset_${UUID.randomUUID()}",
                    message = "Proceed to reset your password"
                )
            }
        }
    }
}