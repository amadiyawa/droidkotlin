package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_base.domain.result.OperationResult

/**
 * Use case for handling social sign-in functionality.
 *
 * This class encapsulates the business logic for performing social sign-in using a specified provider.
 *
 * @property authRepository The authentication repository used to perform the social sign-in.
 */
internal class SocialSignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: SocialProvider): OperationResult<AuthResult> {
        return authRepository.socialSignIn(provider)
    }
}