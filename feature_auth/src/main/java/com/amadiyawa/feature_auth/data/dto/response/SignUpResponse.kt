package com.amadiyawa.feature_auth.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    @SerialName("verificationId") val verificationId: String,
    @SerialName("recipient") val recipient: String,
    @SerialName("expiresIn") val expiresIn: Int
)