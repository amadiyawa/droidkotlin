package com.amadiyawa.feature_auth.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class AuthResponse(
    @SerialName("user")
    val user: UserResponse,

    @SerialName("tokens")
    val tokens: TokenResponse,

    @SerialName("metadata")
    val metadata: Map<String, String>? = null
)

fun AuthResponse.Companion.random(): AuthResponse {
    val randomId = Random.nextInt(1000, 9999).toString()
    val currentTime = System.currentTimeMillis()

    return AuthResponse(
        user = UserResponse(
            id = randomId,
            fullName = "Amadou Iyawa",
            username = "amadiyawa",
            email = "me@amadiyawa.com",
            phoneNumber = "+237699182482",
            avatarUrl = "https://avatars.githubusercontent.com/u/31802381?s=400&u=59fb0139c6b89f16aa9b04e96483eedf11df0796&v=4",
            isEmailVerified = Random.nextBoolean(),
            isPhoneVerified = Random.nextBoolean(),
            roles = setOf("USER").takeIf { Random.nextBoolean() } ?: setOf("USER", "ADMIN"),
            lastLoginAt = Random.nextLong(100000, 1000000),
            isActive = Random.nextBoolean(),
            timezone = "UTC+1",
            locale = "en-US",
            createdAt = currentTime - Random.nextLong(100000, 1000000),
            updatedAt = currentTime,
            providerData = mapOf(
                "Google" to "google_$randomId",
                "Facebook" to "facebook_$randomId",
                "Github" to "github_$randomId"
            ).takeIf { Random.nextBoolean() }
        ),
        tokens = TokenResponse(
            accessToken = "token_$randomId",
            refreshToken = "refresh_$randomId".takeIf { Random.nextBoolean() },
            expiresIn = Random.nextLong(3600, 7200)
        ),
        metadata = mapOf(
            "provider" to listOf("Google", "Facebook", "Github").random(),
            "is_first_login" to Random.nextBoolean().toString()
        )
    )
}