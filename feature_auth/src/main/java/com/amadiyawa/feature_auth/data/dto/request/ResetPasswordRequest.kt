package com.amadiyawa.feature_auth.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(

    @SerialName("verificationId")
    val verificationId: String,

    @SerialName("newPassword")
    val newPassword: String
)