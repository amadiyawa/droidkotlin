package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.result.OperationResult

/**
 * Use case for handling the sign-in operation.
 *
 * This class encapsulates the business logic for signing in a user by delegating
 * the operation to the AuthRepository. It focuses solely on authentication
 * without session management responsibilities.
 *
 * @property authRepository The repository responsible for authentication operations.
 */
internal class SignInUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Authenticates a user with the provided credentials.
     *
     * @param request The sign-in request containing user credentials.
     * @return OperationResult containing AuthResult on success, or failure/error details.
     */
    suspend operator fun invoke(request: SignInRequest): OperationResult<AuthResult> {
        return authRepository.signIn(request)
    }
}