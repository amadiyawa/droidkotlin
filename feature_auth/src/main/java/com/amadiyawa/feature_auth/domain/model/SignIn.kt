package com.amadiyawa.feature_auth.domain.model

data class SignIn(
    val userId: String,
    val username: String,
    val email: String?,
    val phoneNumber: String?,
    val avatarUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val roles: List<String> = emptyList(),
    val token: String,
    val refreshToken: String? = null,
    val issuedAt: Long,
    val expiresAt: Long,
    val createdAt: Long,
    val updatedAt: Long
)
