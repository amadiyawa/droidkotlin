package com.amadiyawa.feature_auth.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import timber.log.Timber

@Serializable
data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String?,
    val phoneNumber: String?,
    val avatarUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val roles: Set<String>,
    val lastLoginAt: Long? = null,
    val isActive: Boolean = true,
    val timezone: String? = null,
    val locale: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val providerData: Map<String, String>? = null,
)

fun User.toJson(): String {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    return json.encodeToString(User.serializer(), this)
}

fun String.toSignIn(): User? {
    return try {
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        json.decodeFromString(User.serializer(), this)
    } catch (e: Exception) {
        Timber.e(e, "Error parsing SignIn from JSON")
        null
    }
}