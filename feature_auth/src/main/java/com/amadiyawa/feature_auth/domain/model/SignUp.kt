package com.amadiyawa.feature_auth.domain.model

data class SignUp(
    val id: String,
    val username: String,
    val email: String,
    val phone: String?,
    val fullName: String?,
    val createdAt: String,
    val updatedAt: String
)
