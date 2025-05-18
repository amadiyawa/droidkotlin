package com.amadiyawa.feature_profile.domain.usecase

import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult

/**
 * Use case for signing out the user
 */
class SignOutUseCase(
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): OperationResult<Unit> {
        return sessionRepository.clearSession()
    }
}