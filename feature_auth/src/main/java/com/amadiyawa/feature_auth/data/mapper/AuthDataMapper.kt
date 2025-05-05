package com.amadiyawa.feature_auth.data.mapper

import com.amadiyawa.feature_auth.data.dto.response.AuthResponse
import com.amadiyawa.feature_auth.data.dto.response.TokenResponse
import com.amadiyawa.feature_auth.data.dto.response.UserResponse
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.model.AuthTokens
import com.amadiyawa.feature_auth.domain.model.User

/**
 * Object responsible for mapping authentication-related data transfer objects (DTOs)
 * to domain models. This ensures a clear separation between the data layer and the
 * domain layer by converting API responses into application-specific models.
 */
internal object AuthDataMapper {
    fun AuthResponse.toDomain(): AuthResult {
        return AuthResult(
            user = user.toDomain(),
            token = tokens.toDomain(),
            metadata = metadata,
        )
    }

    fun UserResponse.toDomain(): User {
        return User(
            id = id,
            fullName = fullName,
            username = username,
            email = email,
            phoneNumber = phoneNumber,
            avatarUrl = avatarUrl,
            isEmailVerified = isEmailVerified,
            isPhoneVerified = isPhoneVerified,
            roles = roles,
            lastLoginAt = lastLoginAt,
            isActive = isActive,
            timezone = timezone,
            locale = locale,
            createdAt = createdAt,
            updatedAt = updatedAt,
            providerData = providerData,
        )
    }

    fun TokenResponse.toDomain(): AuthTokens {
        return AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
            issuedAt = issuedAt,
            tokenType = tokenType
        )
    }
}