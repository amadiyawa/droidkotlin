package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.result.fold
import com.amadiyawa.feature_base.domain.usecase.BaseUseCase

/**
 * Use case for handling the sign-in operation.
 *
 * This class encapsulates the logic for signing in a user by delegating
 * the operation to the `AuthRepository`. It extends the `BaseUseCase` to
 * leverage common error handling and localization functionality.
 *
 * @property authRepository The repository responsible for authentication operations.
 * @property errorLocalizer The localizer for converting errors into user-friendly messages.
 */
internal class SignInUseCase(
    private val authRepository: AuthRepository,
    errorLocalizer: ErrorLocalizer
) : BaseUseCase<AuthResult>(errorLocalizer) {
    suspend operator fun invoke(data: SignInRequest): OperationResult<AuthResult> {
        return authRepository.signIn(data).fold(
            onSuccess = { handleSuccess(it) },
            onFailure = { handleFailure(it) },
            onError = { handleError(it) }
        )
    }
}