package com.amadiyawa.feature_profile.domain.model

/**
 * Data model for user profile
 */
data class UserProfile(
    val id: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String?,
    val avatarUrl: String?,
    val role: String,
    val isEmailVerified: Boolean,
    val isPhoneVerified: Boolean,
    val lastLoginAt: Long?,
    val timezone: String?,
    val locale: String?,

    // Additional fields based on your application needs
    val address: String? = null,
    val accountNumber: String? = null,
    val preferences: Map<String, Any>? = null
)
