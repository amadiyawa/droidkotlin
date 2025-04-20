package com.amadiyawa.feature_auth.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpApiModel(
    @SerialName("verificationId") val verificationId: String,
    @SerialName("recipient") val recipient: String,
    @SerialName("expiresIn") val expiresIn: Int
)

