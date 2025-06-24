package com.amadiyawa.feature_base.domain.repository

import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user session state across the application
 */
interface SessionRepository {
    /**
     * Saves session user data as JSON string to persistent storage
     * @param userJson The JSON string representation of the user
     * @return OperationResult indicating success or failure
     */
    suspend fun saveSessionUserJson(userJson: String): OperationResult<Unit>

    /**
     * Retrieves the current session user as JSON string
     * @return OperationResult containing the user JSON or null if not signed in
     */
    suspend fun getSessionUserJson(): OperationResult<String?>

    /**
     * Updates the session state
     * @param isActive Whether the session is active
     * @return OperationResult indicating success or failure
     */
    suspend fun setSessionActive(isActive: Boolean): OperationResult<Unit>

    /**
     * Returns a flow of the current session state
     * @return Flow emitting whether the session is active
     */
    fun isSessionActive(): Flow<Boolean>

    /**
     * Clears the current session (for sign-out)
     * @return OperationResult indicating success or failure
     */
    suspend fun clearSession(): OperationResult<Unit>
}