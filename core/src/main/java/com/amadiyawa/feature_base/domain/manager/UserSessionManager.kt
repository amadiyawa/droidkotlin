package com.amadiyawa.feature_base.domain.manager

import com.amadiyawa.feature_base.domain.event.DomainEventBus
import com.amadiyawa.feature_base.domain.model.DomainEvent
import com.amadiyawa.feature_base.domain.model.SessionState
import com.amadiyawa.feature_base.domain.model.UserData
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
    private val domainEventBus: DomainEventBus,
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

    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser.asStateFlow()

    val sessionState: Flow<SessionState> = combine(
        currentUser,
        currentRole
    ) { user, role ->
        when {
            user != null && role != UserRole.NONE -> SessionState.Authenticated(user)
            role == UserRole.NONE -> SessionState.NotAuthenticated
            else -> SessionState.Loading
        }
    }

    init {
        // Observe session changes
        observeSession()
        // Observe domain events
        observeDomainEvents()
    }

    /**
     * Initializes the UserSessionManager by loading the current user data
     */
    suspend fun initialize() {
        refreshUserData()
    }

    /**
     * Observe domain events and update session accordingly**
     */
    private fun observeDomainEvents() {
        domainEventBus.subscribe<DomainEvent.UserSignedIn>()
            .onEach { event ->
                _currentUser.value = event.userData
                _currentRole.value = event.userData.role
                _currentUserId.value = event.userData.id
            }
            .launchIn(scope)

        domainEventBus.subscribe<DomainEvent.UserProfileUpdated>()
            .onEach { event ->
                _currentUser.value = event.userData
                _currentRole.value = event.userData.role
                _currentUserId.value = event.userData.id
            }
            .launchIn(scope)

        domainEventBus.subscribe<DomainEvent.UserSignedOut>()
            .onEach {
                _currentUser.value = null
                _currentRole.value = UserRole.NONE
                _currentUserId.value = null
            }
            .launchIn(scope)
    }

    /**
     * Extracts and refreshes the user data from persistent storage
     */
    private suspend fun refreshUserData() = withContext(defaultDispatcher) {
        val userJsonResult = sessionRepository.getSessionUserJson()

        if (userJsonResult is OperationResult.Success && userJsonResult.data != null) {
            try {
                // Parse the FULL user data, not just role and ID
                val userData = parseUserDataFromJson(userJsonResult.data)
                if (userData != null) {
                    _currentUser.value = userData
                    _currentRole.value = userData.role
                    _currentUserId.value = userData.id
                    Timber.d("User data refreshed: ${userData.id}, role: ${userData.role}")
                } else {
                    _currentUser.value = null
                    _currentRole.value = UserRole.NONE
                    _currentUserId.value = null
                    Timber.w("Failed to parse user data from JSON")
                }
            } catch (e: Exception) {
                Timber.e(e, "Error parsing user JSON")
                _currentUser.value = null
                _currentRole.value = UserRole.NONE
                _currentUserId.value = null
            }
        } else {
            _currentUser.value = null
            _currentRole.value = UserRole.NONE
            _currentUserId.value = null
            Timber.d("No session data found")
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
            Timber.Forest.e(e, "Error extracting user data from JSON")
            Pair(UserRole.NONE, null)
        }
    }

    /**
     * Parse full user data from JSON**
     */
    private fun parseUserDataFromJson(jsonString: String): UserData? {
        return try {
            val jsonElement = json.parseToJsonElement(jsonString)
            val authObject = jsonElement.jsonObject
            val userObject = authObject["user"]?.jsonObject
                ?: return null

            object : UserData {
                override val id = userObject["id"]?.jsonPrimitive?.content ?: ""
                override val fullName = userObject["fullName"]?.jsonPrimitive?.content ?: ""
                override val username = userObject["username"]?.jsonPrimitive?.content ?: ""
                override val email = userObject["email"]?.jsonPrimitive?.content ?: ""
                override val phoneNumber = userObject["phoneNumber"]?.jsonPrimitive?.content
                override val avatarUrl = userObject["avatarUrl"]?.jsonPrimitive?.content
                override val role = userObject["role"]?.jsonPrimitive?.content?.let { enumValueOf(it) }
                    ?: UserRole.NONE
                override val isEmailVerified = userObject["isEmailVerified"]?.jsonPrimitive?.content?.toBoolean() ?: false
                override val isPhoneVerified = userObject["isPhoneVerified"]?.jsonPrimitive?.content?.toBoolean() ?: false
                override val lastLoginAt = userObject["lastLoginAt"]?.jsonPrimitive?.content?.toLongOrNull()
                override val isActive = userObject["isActive"]?.jsonPrimitive?.content?.toBoolean() ?: true
                override val timezone = userObject["timezone"]?.jsonPrimitive?.content
                override val locale = userObject["locale"]?.jsonPrimitive?.content
                override val createdAt = userObject["createdAt"]?.jsonPrimitive?.content?.toLongOrNull()
                override val updatedAt = userObject["updatedAt"]?.jsonPrimitive?.content?.toLongOrNull()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error parsing full user data from JSON")
            null
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
                    _currentUser.value = null
                } else if (_currentRole.value == UserRole.NONE || _currentUserId.value == null) {
                    refreshUserData()
                }
            }
            .launchIn(scope)
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