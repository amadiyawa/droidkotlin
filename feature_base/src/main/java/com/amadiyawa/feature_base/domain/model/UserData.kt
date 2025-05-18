package com.amadiyawa.feature_base.domain.model

import com.amadiyawa.feature_base.domain.util.UserRole

/**
 * Core user data interface that all features can use.
 * This represents the essential user information needed across the application.
 */
interface UserData {
    val id: String
    val fullName: String
    val username: String
    val email: String?
    val phoneNumber: String?
    val avatarUrl: String?
    val role: UserRole
    val isEmailVerified: Boolean
    val isPhoneVerified: Boolean
    val lastLoginAt: Long?
    val isActive: Boolean
    val timezone: String?
    val locale: String?
    val createdAt: Long?
    val updatedAt: Long?
}