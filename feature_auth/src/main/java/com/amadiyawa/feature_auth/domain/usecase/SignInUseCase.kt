package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.domain.model.SignIn
import com.amadiyawa.feature_auth.data.datasource.api.model.SignInRequest
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.random.Random

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
        // Simulating network call
        delay(2000)

        return try {
            // Simulate random success/failure
            if (Random.nextInt(0, 100) < 80) {
                // 80% success rate
                OperationResult.Success(SignIn.random())
            } else {
                // 20% failure rate
                val errorMessage = when (Random.nextInt(0, 3)) {
                    0 -> "Invalid credentials"
                    1 -> "Account is locked"
                    else -> "Authentication failed"
                }
                OperationResult.Failure(code = 401, message = errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during sign-in")
            OperationResult.Error(e)
        }
    }
}