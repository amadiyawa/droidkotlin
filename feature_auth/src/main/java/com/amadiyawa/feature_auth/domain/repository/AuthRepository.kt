package com.amadiyawa.feature_auth.domain.repository

import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_base.domain.result.OperationResult

/**
 * Interface for handling authentication operations.
 *
 * This interface defines methods for signing in users through traditional
 * credentials or social providers. It encapsulates the authentication logic
 * and returns the result of the operation.
 */
internal interface AuthRepository {
    /**
     * Signs in a user using the provided credentials.
     *
     * @param signInRequest The request object containing the user's credentials.
     * @return An `OperationResult` containing the authentication result.
     */
    suspend fun signIn(signInRequest: SignInRequest): OperationResult<AuthResult>

    /**
     * Signs in a user using a social provider.
     *
     * @param provider The social provider to be used for authentication.
     * @return An `OperationResult` containing the authentication result.
     */
    suspend fun socialSignIn(provider: SocialProvider): OperationResult<AuthResult>
}