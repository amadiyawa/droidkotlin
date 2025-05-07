package com.amadiyawa.feature_auth.domain.repository

import com.amadiyawa.feature_auth.data.dto.request.ForgotPasswordRequest
import com.amadiyawa.feature_auth.data.dto.request.OtpVerificationRequest
import com.amadiyawa.feature_auth.data.dto.request.ResendOtpRequest
import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.model.OtpVerificationResult
import com.amadiyawa.feature_auth.domain.model.VerificationResult
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
     * @param data The request object containing the user's credentials.
     * @return An `OperationResult` containing the authentication result.
     */
    suspend fun signIn(data: SignInRequest): OperationResult<AuthResult>

    /**
     * Signs in a user using a social provider.
     *
     * @param provider The social provider to be used for authentication.
     * @return An `OperationResult` containing the authentication result.
     */
    suspend fun socialSignIn(provider: SocialProvider): OperationResult<AuthResult>

    /**
     * Initiates the forgot password process for a user.
     *
     * This method sends a request to start the password recovery process
     * for the user identified by the provided data. The result includes
     * a verification response that contains details such as the verification ID
     * and expiration time.
     *
     * @param data The request object containing the user's information for password recovery.
     * @return An `OperationResult` containing the verification response.
     */
    suspend fun forgotPassword(data: ForgotPasswordRequest): OperationResult<VerificationResult>

    suspend fun verifyOtp(data: OtpVerificationRequest): OperationResult<OtpVerificationResult>

    suspend fun resendOtp(request: ResendOtpRequest): OperationResult<VerificationResult>
}