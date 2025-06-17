package com.amadiyawa.feature_auth.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing the result of a password reset operation.
 *
 * This data class contains the essential information returned
 * after a successful password reset (following OTP verification).
 *
 * @property success Indicates if the password reset was successful
 * @property message Success or informational message
 * @property recipient The user's identifier for navigation to sign-in screen
 * @property expiresAt When the new credentials become active (optional)
 * @property resetMethod The method used for reset (otp_verified_token, etc.)
 * @property verificationMethod How the OTP was sent (email, sms)
 * @property timestamp When the reset was completed
 */
@Serializable
data class ResetPasswordResult(
    val success: Boolean,
    val message: String,
    val recipient: String? = null,
    val expiresAt: String? = null,
    val resetMethod: String? = null,
    val verificationMethod: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Check if the reset result indicates a successful operation.
     */
    val isSuccessful: Boolean get() = success

    /**
     * Check if recipient is available for sign-in redirection.
     */
    val hasRecipient: Boolean get() = !recipient.isNullOrBlank()

    /**
     * Check if the reset was done via email verification.
     */
    val wasEmailVerified: Boolean get() = verificationMethod == "email"

    /**
     * Check if the reset was done via SMS verification.
     */
    val wasSmsVerified: Boolean get() = verificationMethod == "sms"
}