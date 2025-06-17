package com.amadiyawa.feature_auth.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response object for password reset operation.
 *
 * This data class represents the response received from the API
 * after a successful password reset operation (after OTP verification).
 *
 * @property success Indicates if the password reset was successful
 * @property message Success or error message from the server
 * @property recipient The user's identifier (email/phone/username) for sign-in redirection
 * @property expiresAt Timestamp when the new credentials become active (optional)
 * @property metadata Additional metadata about the reset operation
 */
@Serializable
data class ResetPasswordResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String,

    @SerialName("recipient")
    val recipient: String? = null,

    @SerialName("expiresAt")
    val expiresAt: String? = null,

    @SerialName("metadata")
    val metadata: Map<String, String>? = null
) {
    companion object {
        /**
         * Generate a random response for testing/simulation purposes.
         */
        fun random(): ResetPasswordResponse {
            val recipients = listOf(
                "amadiyawa@yahoo.com",
                "me@amadiyawa.com",
                "+237699182482",
                "test_user"
            )

            return ResetPasswordResponse(
                success = true,
                message = "Password reset successful",
                recipient = recipients.random(),
                expiresAt = "2024-12-31T23:59:59Z",
                metadata = mapOf(
                    "reset_method" to "otp_verified_token",
                    "verification_method" to if (kotlin.random.Random.nextBoolean()) "email" else "sms",
                    "simulated" to "true",
                    "timestamp" to System.currentTimeMillis().toString()
                )
            )
        }
    }
}