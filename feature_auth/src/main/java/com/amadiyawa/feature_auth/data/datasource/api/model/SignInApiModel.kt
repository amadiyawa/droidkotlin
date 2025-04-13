package com.amadiyawa.feature_auth.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInApiModel(
    @SerialName("user_id") val userId: String,
    @SerialName("username") val username: String,
    @SerialName("email") val email: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("is_email_verified") val isEmailVerified: Boolean = false,
    @SerialName("roles") val roles: List<String> = emptyList(),
    @SerialName("access_token") val token: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("issued_at") val issuedAt: Long,
    @SerialName("expires_at") val expiresAt: Long,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long
)