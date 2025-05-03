package com.amadiyawa.feature_auth.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpVerificationRequest(

    @SerialName("verificationId")
    val verificationId: String,

    @SerialName("code")
    val code: String
)
