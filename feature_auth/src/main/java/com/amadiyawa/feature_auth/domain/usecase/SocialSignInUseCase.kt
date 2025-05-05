package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.result.fold
import com.amadiyawa.feature_base.domain.usecase.BaseUseCase

/**
 * Use case for handling social sign-in functionality.
 *
 * This class encapsulates the business logic for performing social sign-in using a specified provider.
 * It interacts with the authentication repository to execute the operation and processes the result
 * using the `OperationResult` model.
 *
 * @property authRepository The authentication repository used to perform the social sign-in.
 * @property errorLocalizer Handles localization and management of application-specific errors.
 */
internal class SocialSignInUseCase(
    private val authRepository: AuthRepository,
    errorLocalizer: ErrorLocalizer
) : BaseUseCase<AuthResult>(errorLocalizer)  {
    suspend operator fun invoke(provider: SocialProvider): OperationResult<AuthResult> {
        return authRepository.socialSignIn(provider).fold(
            onSuccess = { handleSuccess(it) },
            onFailure = { handleFailure(it) },
            onError = { handleError(it) }
        )
    }
}