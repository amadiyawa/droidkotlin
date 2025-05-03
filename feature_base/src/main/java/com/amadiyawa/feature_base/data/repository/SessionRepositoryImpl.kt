package com.amadiyawa.feature_base.data.repository

import com.amadiyawa.feature_base.data.datastore.DataStoreManager
import com.amadiyawa.feature_base.domain.repository.DataStoreRepository
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber


/**
 * Implementation of SessionRepository that uses DataStore for persistence
 */
class SessionRepositoryImpl(
    private val dataStoreRepository: DataStoreRepository
) : SessionRepository {

    /**
     * Saves session user data to persistent storage
     */
    override suspend fun saveSessionUserJson(userJson: String): OperationResult<Unit> {
        return try {
            // Get existing user (if any)
            val existingUserJson = dataStoreRepository.getData(DataStoreManager.SIGNED_USER_DATA).first() as? String

            // Only save if different from existing
            if (existingUserJson != userJson) {
                dataStoreRepository.saveData(DataStoreManager.SIGNED_USER_DATA, userJson)
            }
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error saving session user data")
            OperationResult.Error(e)
        }
    }

    /**
     * Retrieves the current session user as JSON string
     */
    override suspend fun getSessionUserJson(): OperationResult<String?> {
        return try {
            val userJson = dataStoreRepository.getData(DataStoreManager.SIGNED_USER_DATA).first() as? String
            OperationResult.Success(userJson)
        } catch (e: Exception) {
            Timber.e(e, "Error retrieving session user data")
            OperationResult.Error(e)
        }
    }

    /**
     * Updates the session state
     */
    override suspend fun setSessionActive(isActive: Boolean): OperationResult<Unit> {
        return try {
            dataStoreRepository.saveData(DataStoreManager.IS_USER_SIGNED_IN, isActive)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error updating session state")
            OperationResult.Error(e)
        }
    }

    /**
     * Returns a flow of the current session state
     */
    override fun isSessionActive(): Flow<Boolean> {
        return dataStoreRepository.getData(DataStoreManager.IS_USER_SIGNED_IN)
            .map { it == true }
            .catch {
                Timber.e(it, "Error retrieving session state")
                emit(false)
            }
    }

    /**
     * Clears the current session (for sign-out)
     */
    override suspend fun clearSession(): OperationResult<Unit> {
        return try {
            dataStoreRepository.clearData(DataStoreManager.SIGNED_USER_DATA)
            dataStoreRepository.saveData(DataStoreManager.IS_USER_SIGNED_IN, false)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error clearing session data")
            OperationResult.Error(e)
        }
    }
}