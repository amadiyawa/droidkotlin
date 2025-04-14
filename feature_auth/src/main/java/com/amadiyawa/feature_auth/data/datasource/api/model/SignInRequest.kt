package com.amadiyawa.feature_auth.data.datasource.api.model

data class SignInRequest(
    val identifier: String,
    val password: String
)