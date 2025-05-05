package com.amadiyawa.feature_auth.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(

    @SerialName("recipient")
    val recipient: String
)