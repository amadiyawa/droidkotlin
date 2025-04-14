package com.amadiyawa.feature_auth.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(

    @SerialName("recipient")
    val recipient: String,

    @SerialName("otp")
    val otp: String,

    @SerialName("new_password")
    val newPassword: String
)
