package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.domain.model.SignIn
import com.amadiyawa.feature_auth.domain.model.SignInRequest
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.result.OperationResult

/**
 * Use case for handling the sign-in process.
 *
 * This class encapsulates the logic for signing in a user by delegating
 * the request to the `AuthRepository`.
 *
 * @property authRepository The repository responsible for authentication operations.
 */
internal class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(signInRequest: SignInRequest): OperationResult<SignIn> {
        return authRepository.signIn(signInRequest = signInRequest)
    }
}