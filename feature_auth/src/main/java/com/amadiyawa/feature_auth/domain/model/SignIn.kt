package com.amadiyawa.feature_auth.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber
import kotlin.random.Random

@Serializable
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
) {
    companion object {
        fun random(): SignIn {
            val randomId = Random.nextInt(1000, 9999).toString()
            val randomUsername = "user$randomId"
            val randomEmail = if (Random.nextBoolean()) "user$randomId@amadiyawa.com" else null
            val randomPhone = if (Random.nextBoolean()) "+2376${Random.nextInt(10000000, 99999999)}" else null
            val randomRoles = listOf("USER", "ADMIN").shuffled().take(Random.nextInt(1, 2))
            val currentTime = System.currentTimeMillis()

            return SignIn(
                userId = randomId,
                username = randomUsername,
                email = randomEmail,
                phoneNumber = randomPhone,
                avatarUrl = null,
                isEmailVerified = Random.nextBoolean(),
                roles = randomRoles,
                token = "token_$randomId",
                refreshToken = if (Random.nextBoolean()) "refreshToken_$randomId" else null,
                issuedAt = currentTime,
                expiresAt = currentTime + Random.nextLong(3600000, 7200000),
                createdAt = currentTime - Random.nextLong(100000, 1000000),
                updatedAt = currentTime
            )
        }
    }
}

/**
 * Converts SignIn to a JSON string
 */
fun SignIn.toJson(): String {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    return json.encodeToString(SignIn.serializer(), this)
}

/**
 * Creates SignIn from a JSON string
 */
fun String.toSignIn(): SignIn? {
    return try {
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        json.decodeFromString(SignIn.serializer(), this)
    } catch (e: Exception) {
        Timber.e(e, "Error parsing SignIn from JSON")
        null
    }
}