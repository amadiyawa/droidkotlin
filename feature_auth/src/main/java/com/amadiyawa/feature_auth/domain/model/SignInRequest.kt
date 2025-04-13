package com.amadiyawa.feature_auth.domain.model

data class SignInRequest(
    val identifier: String,
    val password: String
)