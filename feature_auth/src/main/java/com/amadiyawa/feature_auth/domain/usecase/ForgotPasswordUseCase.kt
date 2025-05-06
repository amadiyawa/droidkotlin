package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.data.dto.request.ForgotPasswordRequest
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.result.fold
import com.amadiyawa.feature_base.domain.usecase.BaseUseCase

internal class ForgotPasswordUseCase(
    private val authRepository: AuthRepository,
    errorLocalizer: ErrorLocalizer
) : BaseUseCase<VerificationResult>(errorLocalizer) {
    suspend operator fun invoke(data: ForgotPasswordRequest): OperationResult<VerificationResult> {
        return authRepository.forgotPassword(data).fold(
            onSuccess = { handleSuccess(it) },
            onFailure = { handleFailure(it) },
            onError = { handleError(it) }
        )
    }
}