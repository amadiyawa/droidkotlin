package com.amadiyawa.feature_auth.data.repository

import com.amadiyawa.feature_auth.data.dto.response.AuthResponse
import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.data.dto.response.random
import com.amadiyawa.feature_auth.data.mapper.AuthDataMapper.toDomain
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.UUID
import kotlin.random.Random

/**
 * Simulated implementation of the `AuthRepository` interface.
 *
 * This class provides mock implementations for authentication operations,
 * such as signing in with traditional credentials or social providers.
 * It simulates network delays and random outcomes to mimic the behavior
 * of a real authentication system.
 */
internal class SimulatedAuthRepository : AuthRepository {
    private val simulatedNetworkDelay = 2000L

    override suspend fun signIn(signInRequest: SignInRequest): OperationResult<AuthResult> {
        delay(simulatedNetworkDelay)

        return try {
            when (Random.nextInt(0, 100)) {
                in 0..79 -> simulateSignInSuccess()  // 80% success rate
                else -> simulateSignInFailure(signInRequest)      // 20% failure rate
            }
        } catch (e: Exception) {
            Timber.e(e, "Simulated error during sign-in")
            OperationResult.error(
                throwable = e,
                message = "Simulated system error"
            )
        }
    }

    override suspend fun socialSignIn(provider: SocialProvider): OperationResult<AuthResult> {
        delay(simulatedNetworkDelay)

        return try {
            when (Random.nextInt(0, 100)) {
                in 0..79 -> simulateSuccess(provider)    // 80% success
                else -> simulateFailure(provider)         // 20% failure
            }
        } catch (e: Exception) {
            Timber.e(e, "Simulated error during $provider sign-in")
            OperationResult.error(
                throwable = e,
                message = "Simulated system error"
            )
        }
    }

    private fun simulateSignInSuccess(): OperationResult<AuthResult> {
        return OperationResult.success(
            AuthResponse.random().copy(
                user = AuthResponse.random().user.copy(
                    providerData = null
                ),
                metadata = mapOf(
                    "auth_method" to "email_password",
                    "simulated" to "true"
                )
            ).toDomain()
        )
    }

    private fun simulateSignInFailure(signInRequest: SignInRequest): OperationResult<AuthResult> {
        val (code, message) = when (Random.nextInt(0, 3)) {
            0 -> 401 to "Invalid email or password"
            1 -> 403 to "Account temporarily locked"
            else -> 423 to "Too many login attempts"
        }

        return OperationResult.failure(
            code = code,
            message = message,
            details = mapOf(
                "identifier" to signInRequest.identifier,
                "retry_after" to "5m"
            )
        )
    }

    private fun simulateSuccess(provider: SocialProvider): OperationResult<AuthResult> {
        return OperationResult.success(
            AuthResponse.random().copy(
                user = AuthResponse.random().user.copy(
                    fullName = "${provider.name} User",
                    email = "${provider.name.lowercase()}@simulated.com",
                    providerData = mapOf(
                        provider.name to "${provider.name.lowercase()}_${UUID.randomUUID()}"
                    )
                ),
                metadata = mapOf(
                    "provider" to provider.name,
                    "simulated" to "true"
                )
            ).toDomain()
        )
    }

    private fun simulateFailure(provider: SocialProvider): OperationResult<AuthResult> {
        val (code, message) = when (Random.nextInt(0, 3)) {
            0 -> 503 to "Failed to connect to ${provider.name} servers"
            1 -> 401 to "Authentication cancelled by user"
            else -> 400 to "Invalid ${provider.name} credentials"
        }

        return OperationResult.failure(
            code = code,
            message = message,
            details = mapOf(
                "provider" to provider.name,
                "simulated" to "true",
                "retry_after" to "5m"
            )
        )
    }
}