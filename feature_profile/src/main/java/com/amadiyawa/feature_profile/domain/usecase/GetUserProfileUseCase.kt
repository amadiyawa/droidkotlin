package com.amadiyawa.feature_profile.domain.usecase

import com.amadiyawa.feature_base.domain.model.UserData
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.util.UserRole
import com.amadiyawa.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Use case for retrieving user profile data for ProfileMain screen
 */
class GetUserProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): OperationResult<UserProfileData> = withContext(ioDispatcher) {
        when (val result = profileRepository.getCurrentUser()) {
            is OperationResult.Success -> {
                OperationResult.Success(result.data.toUserProfileData())
            }
            is OperationResult.Error -> result
            is OperationResult.Failure -> result
        }
    }
}

/**
 * Extension function to convert UserData to data needed for ProfileMain
 */
fun UserData.toUserProfileData(): UserProfileData {
    return UserProfileData(
        userName = this.fullName,
        userEmail = this.email,
        userPhone = this.phoneNumber ?: "",
        userAvatarUrl = this.avatarUrl,
        userRole = this.role
    )
}

/**
 * Data class specifically for ProfileMain screen
 */
data class UserProfileData(
    val userName: String,
    val userEmail: String?,
    val userPhone: String,
    val userAvatarUrl: String?,
    val userRole: UserRole
)