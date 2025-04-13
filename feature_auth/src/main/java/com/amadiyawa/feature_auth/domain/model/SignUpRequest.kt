package com.amadiyawa.feature_auth.domain.model

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String?,
    val phone: String?
)