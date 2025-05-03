package com.amadiyawa.feature_auth.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(

    @SerialName("verificationId")
    val verificationId: String,

    @SerialName("newPassword")
    val newPassword: String
)