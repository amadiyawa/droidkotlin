package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_base.domain.model.UserData
import com.amadiyawa.feature_base.domain.util.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class User(
    override val id: String,
    override val fullName: String,
    override val username: String,
    override val email: String?,
    override val phoneNumber: String? = null,
    override val avatarUrl: String? = null,
    override val isEmailVerified: Boolean = false,
    override val isPhoneVerified: Boolean = false,
    override val role: UserRole,
    override val lastLoginAt: Long? = null,
    override val isActive: Boolean = true,
    override val timezone: String? = null,
    override val locale: String? = null,
    override val createdAt: Long? = null,
    override val updatedAt: Long? = null,

    // Additional fields specific to auth feature
    val providerData: Map<String, String>? = null
) : UserData