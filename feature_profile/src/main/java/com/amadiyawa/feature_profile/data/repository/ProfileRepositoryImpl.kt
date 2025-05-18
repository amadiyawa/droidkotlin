package com.amadiyawa.feature_profile.data.repository

import com.amadiyawa.feature_base.domain.event.DomainEventBus
import com.amadiyawa.feature_base.domain.manager.UserSessionManager
import com.amadiyawa.feature_base.domain.model.DomainEvent
import com.amadiyawa.feature_base.domain.model.SessionState
import com.amadiyawa.feature_base.domain.model.UserData
import com.amadiyawa.feature_base.domain.repository.SessionRepository
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import timber.log.Timber

/**
 * Implementation of ProfileRepository that uses SessionRepository and UserSessionManager
 * to manage user profile data.
 */
class ProfileRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val userSessionManager: UserSessionManager,
    private val domainEventBus: DomainEventBus,
    private val json: Json,
    private val ioDispatcher: CoroutineDispatcher
) : ProfileRepository {

    /**
     * Retrieves the current user's profile data from the session.
     */
    override suspend fun getCurrentUser(): OperationResult<UserData> = withContext(ioDispatcher) {
        val currentUser = userSessionManager.currentUser.value
        if (currentUser != null) {
            OperationResult.Success(currentUser)
        } else {
            OperationResult.Failure(
                code = 401,
                message = "User not authenticated"
            )
        }
    }

    /**
     * Updates the current user's profile data in the session and emits domain events.
     */
    override suspend fun updateUserProfile(userData: UserData): OperationResult<Unit> =
        withContext(ioDispatcher) {
            try {
                // Get current session JSON
                when (val sessionResult = sessionRepository.getSessionUserJson()) {
                    is OperationResult.Success -> {
                        sessionResult.data?.let { currentJson ->
                            // Parse, update user object, and save back
                            val jsonElement = json.parseToJsonElement(currentJson)
                            val authObject = jsonElement.jsonObject.toMutableMap()

                            // Update user object with new data
                            authObject["user"] = createUpdatedUserJson(userData)

                            // Save updated JSON
                            val updatedJson = Json.encodeToString(
                                JsonObject.serializer(),
                                JsonObject(authObject)
                            )

                            when (val saveResult = sessionRepository.saveSessionUserJson(updatedJson)) {
                                is OperationResult.Success -> {
                                    // Emit domain event to notify other components
                                    try {
                                        domainEventBus.emit(
                                            DomainEvent.UserProfileUpdated(
                                                userData = userData
                                            )
                                        )
                                    } catch (e: Exception) {
                                        // Don't fail the update if event emission fails
                                        Timber.w(e, "Failed to emit profile update event")
                                    }

                                    Timber.i("User profile updated successfully: ${userData.id}")
                                    OperationResult.Success(Unit)
                                }
                                is OperationResult.Error -> saveResult
                                is OperationResult.Failure -> saveResult
                            }
                        } ?: OperationResult.Failure(
                            code = 404,
                            message = "No session data found"
                        )
                    }
                    is OperationResult.Error -> sessionResult as OperationResult<Unit>
                    is OperationResult.Failure -> sessionResult as OperationResult<Unit>
                }
            } catch (e: Exception) {
                Timber.e(e, "Error updating user profile")
                OperationResult.Error(
                    throwable = e,
                    message = "Failed to update profile"
                )
            }
        }

    /**
     * Updates the current user's avatar.
     * Note: In a real implementation, this would upload the avatar to a server first.
     */
    override suspend fun updateUserAvatar(
        avatarData: ByteArray,
        mimeType: String
    ): OperationResult<String> = withContext(ioDispatcher) {
        try {
            // Get current user
            when (val userResult = getCurrentUser()) {
                is OperationResult.Success -> {
                    // In a real app, you would upload the avatar to a server here
                    // For this example, we'll simulate generating a URL
                    val avatarUrl = "https://example.com/avatars/${userResult.data.id}?updated=${System.currentTimeMillis()}"

                    // Create updated user data with new avatar URL
                    val updatedUserData = createUpdatedUserData(userResult.data, avatarUrl)

                    when (val updateResult = updateUserProfile(updatedUserData)) {
                        is OperationResult.Success -> {
                            // Emit specific avatar updated event
                            try {
                                domainEventBus.emit(
                                    DomainEvent.UserAvatarUpdated(
                                        avatarUrl = avatarUrl,
                                        userId = userResult.data.id
                                    )
                                )
                            } catch (e: Exception) {
                                // Don't fail the update if event emission fails
                                Timber.w(e, "Failed to emit avatar update event")
                            }

                            Timber.i("User avatar updated successfully: ${userResult.data.id}")
                            OperationResult.Success(avatarUrl)
                        }
                        is OperationResult.Error -> updateResult as OperationResult<String>
                        is OperationResult.Failure -> updateResult as OperationResult<String>
                    }
                }
                is OperationResult.Error -> userResult as OperationResult<String>
                is OperationResult.Failure -> userResult as OperationResult<String>
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating user avatar")
            OperationResult.Error(
                throwable = e,
                message = "Failed to update avatar"
            )
        }
    }

    /**
     * Observes changes to the current user's profile.
     */
    override fun observeCurrentUser(): Flow<UserData?> {
        return userSessionManager.sessionState.map { state ->
            when (state) {
                is SessionState.Authenticated -> state.user
                else -> null
            }
        }
    }

    /**
     * Creates an updated user JSON object from UserData.
     */
    private fun createUpdatedUserJson(userData: UserData): JsonObject {
        val userMap = buildMap<String, JsonPrimitive> {
            put("id", JsonPrimitive(userData.id))
            put("fullName", JsonPrimitive(userData.fullName))
            put("username", JsonPrimitive(userData.username))
            put("email", JsonPrimitive(userData.email))

            userData.phoneNumber?.let { put("phoneNumber", JsonPrimitive(it)) }
            userData.avatarUrl?.let { put("avatarUrl", JsonPrimitive(it)) }
            put("role", JsonPrimitive(userData.role.name))
            put("isEmailVerified", JsonPrimitive(userData.isEmailVerified))
            put("isPhoneVerified", JsonPrimitive(userData.isPhoneVerified))
            userData.lastLoginAt?.let { put("lastLoginAt", JsonPrimitive(it)) }
            put("isActive", JsonPrimitive(userData.isActive))
            userData.timezone?.let { put("timezone", JsonPrimitive(it)) }
            userData.locale?.let { put("locale", JsonPrimitive(it)) }
            userData.createdAt?.let { put("createdAt", JsonPrimitive(it)) }
            put("updatedAt", JsonPrimitive(System.currentTimeMillis()))
        }

        return JsonObject(userMap)
    }

    /**
     * Creates updated UserData with new avatar URL.
     */
    private fun createUpdatedUserData(original: UserData, newAvatarUrl: String): UserData {
        return object : UserData by original {
            override val avatarUrl = newAvatarUrl
            override val updatedAt = System.currentTimeMillis()
        }
    }
}