package com.amadiyawa.feature_profile.domain.usecase

import com.amadiyawa.feature_base.domain.model.UserData
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Use case for checking what features user has access to
 */
class CheckUserPermissionsUseCase(
    private val profileRepository: ProfileRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): OperationResult<UserPermissions> = withContext(ioDispatcher) {
        when (val result = profileRepository.getCurrentUser()) {
            is OperationResult.Success -> {
                val permissions = UserPermissions(
                    hasEditProfileAccess = determineEditProfileAccess(result.data),
                    hasSettingsAccess = true, // Usually everyone has settings access
                    hasSignOutAccess = true   // Everyone can sign out
                )
                OperationResult.Success(permissions)
            }
            is OperationResult.Error -> result
            is OperationResult.Failure -> result
        }
    }

    private fun determineEditProfileAccess(userData: UserData): Boolean {
        // Business logic for edit profile access
        return userData.isEmailVerified && userData.isActive
    }
}

/**
 * Data class for user permissions
 */
data class UserPermissions(
    val hasEditProfileAccess: Boolean,
    val hasSettingsAccess: Boolean,
    val hasSignOutAccess: Boolean
)