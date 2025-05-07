package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.data.dto.request.OtpVerificationRequest
import com.amadiyawa.feature_auth.domain.model.OtpVerificationResult
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.result.fold
import com.amadiyawa.feature_base.domain.usecase.BaseUseCase

internal class OtpVerificationUseCase(
    private val authRepository: AuthRepository,
    errorLocalizer: ErrorLocalizer
) : BaseUseCase<OtpVerificationResult>(errorLocalizer) {
    suspend operator fun invoke(data: OtpVerificationRequest): OperationResult<OtpVerificationResult> {
        return authRepository.verifyOtp(data).fold(
            onSuccess = { handleSuccess(it) },
            onFailure = { handleFailure(it) },
            onError = { handleError(it) }
        )
    }
}