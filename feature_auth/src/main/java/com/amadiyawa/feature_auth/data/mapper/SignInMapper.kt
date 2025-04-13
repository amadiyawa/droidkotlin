package com.amadiyawa.feature_auth.data.mapper

import com.amadiyawa.feature_auth.data.datasource.api.model.SignInApiModel
import com.amadiyawa.feature_auth.domain.model.SignIn

internal fun SignInApiModel.toDomainModel() = SignIn(
    userId = userId,
    username = username,
    email = email,
    phoneNumber = phoneNumber,
    avatarUrl = avatarUrl,
    isEmailVerified = isEmailVerified,
    roles = roles,
    token = token,
    refreshToken = refreshToken,
    issuedAt = issuedAt,
    expiresAt = expiresAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)