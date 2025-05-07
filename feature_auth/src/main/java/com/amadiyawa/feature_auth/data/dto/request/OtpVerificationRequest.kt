package com.amadiyawa.feature_auth.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpVerificationRequest(

    @SerialName("verificationId")
    val verificationId: String,

    @SerialName("code")
    val code: String,

    val purpose: String
)