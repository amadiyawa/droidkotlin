package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.data.dto.request.SignInRequest
import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.model.toJson
import com.amadiyawa.feature_base.domain.event.DomainEventBus
import com.amadiyawa.feature_base.domain.model.DomainEvent
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import timber.log.Timber

/**
 * Complete sign-in use case that handles the entire sign-in flow:
 * 1. User authentication
 * 2. Session saving
 * 3. Session activation
 * 4. Domain event emission
 */
internal class CompleteSignInUseCase(
    private val signInUseCase: SignInUseCase,
    private val sessionRepository: SessionRepository,
    private val domainEventBus: DomainEventBus
) {
    /**
     * Executes the complete sign-in flow.
     */
    suspend operator fun invoke(request: SignInRequest): OperationResult<AuthResult> {
        // Step 1: Authenticate user
        return when (val authResult = signInUseCase(request)) {
            is OperationResult.Success -> {
                // Step 2: Save session
                when (val saveResult = sessionRepository.saveSessionUserJson(authResult.data.toJson())) {
                    is OperationResult.Success -> {
                        // Step 3: Activate session
                        when (val activateResult = sessionRepository.setSessionActive(true)) {
                            is OperationResult.Success -> {
                                // Step 4: Emit domain event
                                try {
                                    domainEventBus.emit(
                                        DomainEvent.UserSignedIn(
                                            userData = authResult.data.user,
                                            isFirstTimeUser = authResult.data.metadata?.get("is_first_login")?.toString()?.toBoolean() == true
                                        )
                                    )
                                } catch (e: Exception) {
                                    // Don't fail the sign-in if event emission fails
                                    Timber.w(e, "Failed to emit sign-in domain event")
                                }

                                Timber.i("User signed in successfully: ${authResult.data.user.id}")
                                OperationResult.Success(authResult.data)
                            }
                            is OperationResult.Error -> activateResult
                            is OperationResult.Failure -> activateResult
                        }
                    }
                    is OperationResult.Error -> saveResult
                    is OperationResult.Failure -> saveResult
                }
            }
            is OperationResult.Error -> authResult
            is OperationResult.Failure -> authResult
        }
    }
}