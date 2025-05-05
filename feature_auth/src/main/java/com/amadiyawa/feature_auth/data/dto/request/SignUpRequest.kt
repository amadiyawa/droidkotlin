package com.amadiyawa.feature_auth.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(

    @SerialName("username")
    val username: String,

    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String,

    @SerialName("full_name")
    val fullName: String?,

    @SerialName("phone")
    val phone: String?
)