package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_auth.domain.util.OtpPurpose
import com.amadiyawa.feature_auth.domain.util.VerificationType
import kotlinx.serialization.Serializable

@Serializable
data class VerificationResult(
    val verificationId: String,
    val recipient: String,
    val expiresIn: Int,
    val type: VerificationType,
    var purpose: OtpPurpose = OtpPurpose.SIGN_UP,
    val metadata: Map<String, String>? = null,
)