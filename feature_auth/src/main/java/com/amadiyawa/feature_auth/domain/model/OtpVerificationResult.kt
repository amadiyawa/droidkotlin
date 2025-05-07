package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_auth.data.dto.response.AuthResponse

data class OtpVerificationResult(
    val success: Boolean,
    val purpose: String,
    val message: String? = null,
    val authResponse: AuthResponse? = null,
    val resetToken: String? = null,
)