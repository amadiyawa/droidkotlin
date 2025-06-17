package com.amadiyawa.feature_auth.data.repository

import com.amadiyawa.feature_auth.data.dto.request.ForgotPasswordRequest
import com.amadiyawa.feature_auth.data.dto.request.OtpVerificationRequest
import com.amadiyawa.feature_auth.data.dto.request.ResendOtpRequest
import com.amadiyawa.feature_auth.data.dto.request.ResetPasswordRequest
import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.data.dto.response.AuthResponse
import com.amadiyawa.feature_auth.data.dto.response.OtpVerificationResponse
import com.amadiyawa.feature_auth.data.dto.response.ResetPasswordResponse
import com.amadiyawa.feature_auth.data.dto.response.VerificationResponse
import com.amadiyawa.feature_auth.data.dto.response.random
import com.amadiyawa.feature_auth.data.mapper.AuthDataMapper.toDomain
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.model.OtpVerificationResult
import com.amadiyawa.feature_auth.domain.model.ResetPasswordResult
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_auth.domain.util.OtpPurpose
import com.amadiyawa.feature_auth.domain.util.SocialProvider
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

    override suspend fun forgotPassword(request: ForgotPasswordRequest): OperationResult<VerificationResult> {
        delay(simulatedNetworkDelay)

        return try {
            when (Random.nextInt(0, 100)) {
                in 0..89 -> OperationResult.success(
                    VerificationResponse.random(OtpPurpose.PASSWORD_RESET.name).toDomain()
                )
                else -> simulateForgotPasswordFailure(request)
            }
        } catch (e: Exception) {
            Timber.e(e, "Simulated error during forgot password")
            OperationResult.error(
                throwable = e,
                message = "Simulated system error"
            )
        }
    }

    private fun simulateForgotPasswordFailure(request: ForgotPasswordRequest): OperationResult<VerificationResult> {
        val (code, message) = when (Random.nextInt(0, 3)) {
            0 -> 404 to "Account not found"
            1 -> 429 to "Too many password reset attempts"
            else -> 503 to "Email service temporarily unavailable"
        }

        return OperationResult.failure(
            code = code,
            message = message,
            details = mapOf(
                "recipient" to request.recipient,
                "retry_after" to "15m"
            )
        )
    }

    override suspend fun verifyOtp(request: OtpVerificationRequest): OperationResult<OtpVerificationResult> {
        delay(simulatedNetworkDelay)

        return try {
            when (request.code) {
                "000000" -> simulateOtpSystemError()
                "999999" -> simulateOtpFailure(request)
                else -> {
                    OperationResult.success(
                        OtpVerificationResponse.randomSuccess(request.purpose).toDomain()
                    )
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Simulated error during OTP verification")
            OperationResult.error(
                throwable = e,
                message = "Simulated system error"
            )
        }
    }

    private fun simulateOtpSystemError(): OperationResult<OtpVerificationResult> {
        return OperationResult.error(
            throwable = RuntimeException("OTP service unavailable"),
            message = "System error during verification",
            code = 503
        )
    }

    private fun simulateOtpFailure(request: OtpVerificationRequest): OperationResult<OtpVerificationResult> {
        return OperationResult.failure(
            code = 400,
            message = if (Random.nextBoolean()) "Invalid OTP code" else "Expired OTP",
            details = mapOf(
                "verificationId" to request.verificationId,
                "purpose" to request.purpose
            )
        )
    }

    override suspend fun resendOtp(request: ResendOtpRequest): OperationResult<VerificationResult> {
        delay(simulatedNetworkDelay)

        return try {
            when (Random.nextInt(0, 100)) {
                in 0..89 -> simulateResendOtpSuccess(request)  // 90% success rate
                else -> simulateResendOtpFailure(request)      // 10% failure rate
            }
        } catch (e: Exception) {
            Timber.e(e, "Simulated error during OTP resend")
            OperationResult.error(
                throwable = e,
                message = "Simulated system error during OTP resend"
            )
        }
    }

    private fun simulateResendOtpSuccess(request: ResendOtpRequest): OperationResult<VerificationResult> {
        return OperationResult.success(
            VerificationResponse.random(request.purpose).copy(
                purpose = request.purpose,
                metadata = mapOf(
                    "resent" to "true",
                    "previous_verification_id" to request.verificationId,
                    "simulated" to "true"
                )
            ).toDomain()
        )
    }

    private fun simulateResendOtpFailure(request: ResendOtpRequest): OperationResult<VerificationResult> {
        val (code, message) = when (Random.nextInt(0, 3)) {
            0 -> 404 to "Verification ID not found"
            1 -> 429 to "Too many OTP resend attempts"
            else -> 503 to "OTP service temporarily unavailable"
        }

        return OperationResult.failure(
            code = code,
            message = message,
            details = mapOf(
                "verificationId" to request.verificationId,
                "purpose" to request.purpose,
                "retry_after" to "30s"
            )
        )
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): OperationResult<ResetPasswordResult> {
        delay(simulatedNetworkDelay)

        return try {
            when {
                // Simulate invalid reset token (not from OTP verification)
                request.resetToken.startsWith("invalid_") -> simulateInvalidResetToken(request)

                // Simulate expired reset token
                request.resetToken.startsWith("expired_") -> simulateExpiredResetToken(request)

                // Simulate token not from OTP verification (security check)
                !request.resetToken.startsWith("otp_verified_") -> simulateUnauthorizedToken(request)

                // Simulate weak password (for testing)
                request.newPassword.length < 8 -> simulateWeakPassword(request)

                // Simulate success (90% of valid cases)
                Random.nextInt(0, 100) < 90 -> simulateResetSuccess(request)

                // Simulate general failure (10% of cases)
                else -> simulateResetFailure(request)
            }
        } catch (e: Exception) {
            Timber.e(e, "Simulated error during password reset")
            OperationResult.error(
                throwable = e,
                message = "Simulated system error during password reset"
            )
        }
    }

    private fun simulateInvalidResetToken(request: ResetPasswordRequest): OperationResult<ResetPasswordResult> {
        return OperationResult.failure(
            code = 400,
            message = "Invalid reset token",
            details = mapOf(
                "token" to request.resetToken,
                "reason" to "token_invalid",
                "hint" to "Token must be obtained from OTP verification"
            )
        )
    }

    private fun simulateExpiredResetToken(request: ResetPasswordRequest): OperationResult<ResetPasswordResult> {
        return OperationResult.failure(
            code = 410,
            message = "Reset token has expired",
            details = mapOf(
                "token" to request.resetToken,
                "reason" to "token_expired",
                "expired_at" to "2024-01-01T00:00:00Z",
                "hint" to "Please request a new password reset"
            )
        )
    }

    private fun simulateUnauthorizedToken(request: ResetPasswordRequest): OperationResult<ResetPasswordResult> {
        return OperationResult.failure(
            code = 401,
            message = "Reset token not authorized",
            details = mapOf(
                "token" to request.resetToken,
                "reason" to "token_not_from_otp_verification",
                "hint" to "Reset token must be obtained after successful OTP verification"
            )
        )
    }

    private fun simulateWeakPassword(request: ResetPasswordRequest): OperationResult<ResetPasswordResult> {
        return OperationResult.failure(
            code = 422,
            message = "Password does not meet security requirements",
            details = mapOf(
                "reason" to "password_too_weak",
                "min_length" to "8",
                "requirements" to "uppercase, lowercase, digit, special character"
            )
        )
    }

    private fun simulateResetSuccess(request: ResetPasswordRequest): OperationResult<ResetPasswordResult> {
        return OperationResult.success(
            ResetPasswordResponse.random().copy(
                message = "Password has been reset successfully. You can now sign in with your new password.",
                metadata = mapOf(
                    "reset_method" to "otp_verified_token",
                    "verification_method" to if (Random.nextBoolean()) "email" else "sms",
                    "token_used" to request.resetToken,
                    "simulated" to "true",
                    "timestamp" to System.currentTimeMillis().toString()
                )
            ).toDomain()
        )
    }

    private fun simulateResetFailure(request: ResetPasswordRequest): OperationResult<ResetPasswordResult> {
        val (code, message) = when (Random.nextInt(0, 3)) {
            0 -> 429 to "Too many password reset attempts"
            1 -> 503 to "Password reset service temporarily unavailable"
            else -> 500 to "Internal server error during password reset"
        }

        return OperationResult.failure(
            code = code,
            message = message,
            details = mapOf(
                "token" to request.resetToken,
                "retry_after" to "5m",
                "simulated" to "true"
            )
        )
    }
}