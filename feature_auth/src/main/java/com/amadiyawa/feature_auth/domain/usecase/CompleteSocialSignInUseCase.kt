package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.domain.model.AuthResult
import com.amadiyawa.feature_auth.domain.model.toJson
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_base.domain.event.DomainEventBus
import com.amadiyawa.feature_base.domain.model.DomainEvent
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import timber.log.Timber

/**
 * Complete social sign-in use case that handles the entire social sign-in flow:
 * 1. Social authentication
 * 2. Session saving
 * 3. Session activation
 * 4. Domain event emission
 */
internal class CompleteSocialSignInUseCase(
    private val socialSignInUseCase: SocialSignInUseCase,
    private val sessionRepository: SessionRepository,
    private val domainEventBus: DomainEventBus
) {
    /**
     * Executes the complete social sign-in flow.
     */
    suspend operator fun invoke(provider: SocialProvider): OperationResult<AuthResult> {
        // Step 1: Authenticate with social provider
        return when (val authResult = socialSignInUseCase(provider)) {
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
                                    Timber.w(e, "Failed to emit social sign-in domain event")
                                }

                                Timber.i("User signed in successfully via ${provider.name}: ${authResult.data.user.id}")
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