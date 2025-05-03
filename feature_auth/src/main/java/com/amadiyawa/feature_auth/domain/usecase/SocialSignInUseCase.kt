package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.domain.enum.SocialProvider
import com.amadiyawa.feature_auth.domain.model.SignIn
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.random.Random

internal class SocialSignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(provider: SocialProvider): OperationResult<SignIn> {
        // Simulating network delay
        delay(1500)

        return try {
            // Simulate random success/failure
            if (Random.nextInt(0, 100) < 80) {
                // 80% success rate
                OperationResult.Success(SignIn.random())
            } else {
                // 20% failure rate
                val errorMessage = when (Random.nextInt(0, 3)) {
                    0 -> "Failed to connect to ${provider.name.lowercase()} servers"
                    1 -> "Authentication cancelled by user"
                    else -> "Authentication with ${provider.name.lowercase()} failed"
                }
                val errorCode = when (Random.nextInt(0, 3)) {
                    0 -> 503 // Service unavailable
                    1 -> 401 // Unauthorized
                    else -> 400 // Bad request
                }
                OperationResult.Failure(code = errorCode, message = errorMessage)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during social sign-in with $provider")
            OperationResult.Error(e)
        }
    }
}