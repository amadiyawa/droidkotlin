package com.amadiyawa.feature_base.presentation.errorhandling

import com.amadiyawa.feature_base.data.repository.AndroidErrorLocalizer
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.feature_base.domain.result.OperationResult

/**
 * Centralized error handler for consistent error handling across the application
 */
class ErrorHandler(
    private val errorLocalizer: ErrorLocalizer
) {
    /**
     * Get localized message from OperationResult
     */
    fun getLocalizedMessage(result: OperationResult<*>): String {
        return when (result) {
            is OperationResult.Error -> {
                errorLocalizer.getLocalizedMessage(
                    errorCode = result.code ?: AndroidErrorLocalizer.ErrorCode.InternalServerError.code,
                    defaultMessage = result.message ?: "An unexpected error occurred"
                )
            }
            is OperationResult.Failure -> {
                errorLocalizer.getLocalizedMessage(
                    errorCode = result.code ?: AndroidErrorLocalizer.ErrorCode.BadRequest.code,
                    defaultMessage = result.message ?: "Operation failed"
                )
            }
            is OperationResult.Success -> ""
        }
    }

    /**
     * Get user-friendly error for specific error types
     */
    fun getNetworkErrorMessage(): String {
        return errorLocalizer.getLocalizedMessage(
            AndroidErrorLocalizer.ErrorCode.NetworkUnavailable.code,
            "Please check your internet connection"
        )
    }

    fun getAuthenticationErrorMessage(): String {
        return errorLocalizer.getLocalizedMessage(
            AndroidErrorLocalizer.ErrorCode.AuthenticationFailed.code,
            "Authentication failed"
        )
    }

    fun getSessionExpiredMessage(): String {
        return errorLocalizer.getLocalizedMessage(
            AndroidErrorLocalizer.ErrorCode.SessionExpired.code,
            "Your session has expired. Please sign in again."
        )
    }
}