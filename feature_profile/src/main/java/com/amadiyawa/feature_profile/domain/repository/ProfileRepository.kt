package com.amadiyawa.feature_profile.domain.repository

import com.amadiyawa.feature_base.domain.model.UserData
import com.amadiyawa.feature_base.domain.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user profile data.
 * Handles all profile-related operations for the currently authenticated user.
 */
interface ProfileRepository {
    /**
     * Retrieves the current user's profile data.
     * @return OperationResult containing the user data or error details
     */
    suspend fun getCurrentUser(): OperationResult<UserData>

    /**
     * Updates the current user's profile data.
     * @param userData The updated user profile data
     * @return OperationResult indicating success or error details
     */
    suspend fun updateUserProfile(userData: UserData): OperationResult<Unit>

    /**
     * Updates the current user's avatar.
     * @param avatarData The avatar image data as ByteArray
     * @param mimeType The mime type of the image (e.g., "image/jpeg", "image/png")
     * @return OperationResult containing the new avatar URL or error details
     */
    suspend fun updateUserAvatar(
        avatarData: ByteArray,
        mimeType: String
    ): OperationResult<String>

    /**
     * Observes changes to the current user's profile.
     * @return Flow of UserData that emits whenever the profile changes
     */
    fun observeCurrentUser(): Flow<UserData?>
}