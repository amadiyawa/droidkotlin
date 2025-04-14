package com.amadiyawa.feature_auth.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpVerificationRequest(

    @SerialName("recipient")
    val recipient: String,

    @SerialName("code")
    val code: String
)
