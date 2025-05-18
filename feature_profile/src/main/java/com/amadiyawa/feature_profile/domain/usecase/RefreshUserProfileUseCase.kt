package com.amadiyawa.feature_profile.domain.usecase

import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Use case for refreshing user profile data
 */
class RefreshUserProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): OperationResult<UserProfileData> = withContext(ioDispatcher) {
        // Since your repository already gets current data from session,
        // this will effectively refresh from the current session state
        when (val result = profileRepository.getCurrentUser()) {
            is OperationResult.Success -> {
                OperationResult.Success(result.data.toUserProfileData())
            }
            is OperationResult.Error -> result
            is OperationResult.Failure -> result
        }
    }
}