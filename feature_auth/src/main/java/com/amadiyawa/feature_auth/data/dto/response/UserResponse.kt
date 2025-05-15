package com.amadiyawa.feature_auth.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("user_id")
    val id: String,

    @SerialName("fullname")
    val fullName: String,

    @SerialName("username")
    val username: String,

    @SerialName("email")
    val email: String? = null,

    @SerialName("phone_number")
    val phoneNumber: String? = null,

    @SerialName("avatar_url")
    val avatarUrl: String? = null,

    @SerialName("is_email_verified")
    val isEmailVerified: Boolean = false,

    @SerialName("is_phone_verified")
    val isPhoneVerified: Boolean = false,

    @SerialName("role")
    val role: String,

    @SerialName("last_login_at")
    val lastLoginAt: Long? = null,

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("timezone")
    val timezone: String? = null,

    @SerialName("locale")
    val locale: String? = null,

    @SerialName("created_at")
    val createdAt: Long,

    @SerialName("updated_at")
    val updatedAt: Long,

    @SerialName("provider_data")
    val providerData: Map<String, String>? = null,
)