package com.amadiyawa.feature_profile.domain.usecase

import com.amadiyawa.feature_base.domain.event.DomainEventBus
import com.amadiyawa.feature_base.domain.manager.UserSessionManager
import com.amadiyawa.feature_base.domain.model.DomainEvent
import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Use case for signing out the current user
 */
class SignOutUserUseCase(
    private val userSessionManager: UserSessionManager,
    private val domainEventBus: DomainEventBus,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(reason: String = "User initiated sign out"): OperationResult<Unit> =
        withContext(ioDispatcher) {
            try {
                // Get current user ID before clearing session (since clearSession will reset it)
                val currentUserId = userSessionManager.currentUserId.value

                // Clear the session data from persistent storage
                userSessionManager.clearSession()

                // Emit domain event to notify all subscribers about the sign out
                domainEventBus.emit(
                    DomainEvent.UserSignedOut(
                        reason = reason,
                        userId = currentUserId
                    )
                )

                OperationResult.Success(Unit)
            } catch (e: Exception) {
                OperationResult.Error(
                    throwable = e,
                    message = "Failed to sign out: ${e.message}"
                )
            }
        }
}