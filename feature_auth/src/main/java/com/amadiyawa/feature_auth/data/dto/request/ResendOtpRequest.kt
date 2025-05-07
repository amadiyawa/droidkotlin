package com.amadiyawa.feature_auth.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResendOtpRequest(
    @SerialName("verificationId")
    val verificationId: String,

    val purpose: String
)
