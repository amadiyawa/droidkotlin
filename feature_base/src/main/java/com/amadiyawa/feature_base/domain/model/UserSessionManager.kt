package com.amadiyawa.feature_base.domain.model

import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.domain.util.UserRole
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber

/**
 * Manager class that handles the current user session and observes changes
 * to update the navigation UI accordingly.
 */
class UserSessionManager(
    private val sessionRepository: SessionRepository,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) {
    // Coroutine scope for background operations
    private val scope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    // Private mutable state flow to store the current user role
    private val _currentRole = MutableStateFlow(UserRole.NONE)

    // Public immutable state flow that can be observed
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Private mutable state flow to store the current user ID
    private val _currentUserId = MutableStateFlow<String?>(null)

    // Public immutable state flow that can be observed
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    init {
        // Observe session changes
        observeSession()
    }

    /**
     * Initializes the UserSessionManager by loading the current user data
     */
    suspend fun initialize() {
        refreshUserData()
    }

    /**
     * Extracts and refreshes the user data from persistent storage
     */
    private suspend fun refreshUserData() = withContext(defaultDispatcher) {
        val userJsonResult = sessionRepository.getSessionUserJson()

        if (userJsonResult is OperationResult.Success && userJsonResult.data != null) {
            try {
                val parsedData = extractUserDataFromJson(userJsonResult.data)
                _currentRole.value = parsedData.first
                _currentUserId.value = parsedData.second
            } catch (e: Exception) {
                Timber.e(e, "Error parsing user JSON")
                _currentRole.value = UserRole.NONE
                _currentUserId.value = null
            }
        } else {
            _currentRole.value = UserRole.NONE
            _currentUserId.value = null
        }
    }

    /**
     * Extracts the user role and ID from the stored JSON string
     * @return Pair of (UserRole, userId?)
     */
    private fun extractUserDataFromJson(userJson: String): Pair<UserRole, String?> {
        return try {
            val jsonElement = json.parseToJsonElement(userJson)
            val authResult = jsonElement.jsonObject

            // Extract user object
            val userObject = authResult["user"]?.jsonObject

            // Extract role from user object
            val roleString = userObject?.get("role")?.jsonPrimitive?.content
            val role = mapStringToUserRole(roleString)

            // Extract ID from user object
            val userId = userObject?.get("id")?.jsonPrimitive?.content

            Pair(role, userId)
        } catch (e: Exception) {
            Timber.e(e, "Error extracting user data from JSON")
            Pair(UserRole.NONE, null)
        }
    }

    /**
     * Maps a role string to UserRole enum
     */
    private fun mapStringToUserRole(role: String?): UserRole {
        if (role == null) return UserRole.NONE

        return when (role.uppercase()) {
            "CLIENT" -> UserRole.CLIENT
            "AGENT" -> UserRole.AGENT
            "ADMIN" -> UserRole.ADMIN
            else -> UserRole.NONE
        }
    }

    /**
     * Observes changes to the session state
     */
    private fun observeSession() {
        // Create a flow that emits whenever session status changes or session content changes
        sessionRepository.isSessionActive()
            .onEach { isActive ->
                if (!isActive) {
                    _currentRole.value = UserRole.NONE
                    _currentUserId.value = null
                } else if (_currentRole.value == UserRole.NONE || _currentUserId.value == null) {
                    refreshUserData()
                }
            }
            .launchIn(scope)
    }

    /**
     * Gets the user role as a Flow, re-evaluated each time
     * the session changes
     */
    fun observeUserRole(): Flow<UserRole?> {
        return sessionRepository.isSessionActive().combine(currentRole) { isActive, role ->
            if (isActive) role else null
        }
    }

    /**
     * Gets the user ID as a Flow, re-evaluated each time
     * the session changes
     */
    fun observeUserId(): Flow<String?> {
        return sessionRepository.isSessionActive().combine(currentUserId) { isActive, userId ->
            if (isActive) userId else null
        }
    }

    /**
     * Checks if the current user has permission to access a specific feature
     * @param requiredRoles The roles that have access to the feature
     * @return True if the user has one of the required roles, false otherwise
     */
    fun hasPermission(vararg requiredRoles: UserRole): Boolean {
        val currentRole = _currentRole.value
        // UserRole.NONE should not grant any permissions
        return currentRole != UserRole.NONE && requiredRoles.contains(currentRole)
    }

    /**
     * Clears the current user session on logout
     */
    suspend fun clearSession() {
        sessionRepository.clearSession()
        _currentRole.value = UserRole.NONE
    }
}