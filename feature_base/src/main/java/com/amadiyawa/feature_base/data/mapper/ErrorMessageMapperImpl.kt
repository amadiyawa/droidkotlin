package com.amadiyawa.feature_base.data.mapper

import com.amadiyawa.feature_base.domain.mapper.ErrorMessageMapper
import com.amadiyawa.feature_base.domain.resources.ResourceProvider
import com.amadiyawa.feature_base.domain.util.ErrorCategory
import com.amadiyawa.feature_base.domain.util.ErrorCodes
import com.amadiyawa.paygo.base.R

// In feature_base/data/mapper/ErrorMessageMapperImpl.kt
class ErrorMessageMapperImpl(
    private val resourceProvider: ResourceProvider
) : ErrorMessageMapper {

    override fun getMessage(
        code: Int?,
        defaultMessageResId: Int,
        category: ErrorCategory?
    ): String {
        if (code == null) return resourceProvider.getString(defaultMessageResId)

        val resId = when (category) {
            ErrorCategory.AUTHENTICATION -> getAuthErrorResId(code)
            ErrorCategory.SOCIAL_AUTH -> getSocialAuthErrorResId(code)
            ErrorCategory.SESSION -> getSessionErrorResId(code)
            ErrorCategory.NETWORK -> getNetworkErrorResId(code)
            null -> {
                getAuthErrorResId(code)
                    ?: getSocialAuthErrorResId(code)
                    ?: getSessionErrorResId(code)
                    ?: getNetworkErrorResId(code)
            }
        } ?: return resourceProvider.getString(defaultMessageResId)

        return resourceProvider.getString(resId)
    }

    private fun getAuthErrorResId(code: Int): Int? {
        return when (code) {
            ErrorCodes.INVALID_CREDENTIALS -> R.string.error_invalid_credentials
            ErrorCodes.SESSION_EXPIRED -> R.string.error_session_expired
            ErrorCodes.UNAUTHORIZED_ACCESS -> R.string.error_unauthorized
            ErrorCodes.ACCOUNT_NOT_FOUND -> R.string.error_account_not_found
            ErrorCodes.TOO_MANY_ATTEMPTS -> R.string.error_too_many_attempts
            else -> null
        }
    }

    private fun getSocialAuthErrorResId(code: Int): Int? {
        return when (code) {
            ErrorCodes.SOCIAL_ACCOUNT_NOT_LINKED -> R.string.error_social_account_not_linked
            ErrorCodes.SOCIAL_AUTH_CANCELLED -> R.string.error_social_auth_cancelled
            ErrorCodes.SOCIAL_AUTH_CONNECTION_FAILED -> R.string.error_social_auth_connection_failed
            else -> null
        }
    }

    private fun getSessionErrorResId(code: Int): Int? {
        return when (code) {
            ErrorCodes.SESSION_SAVE_FAILED -> R.string.error_session_save_failed
            ErrorCodes.SESSION_ACTIVATION_FAILED -> R.string.error_session_activation_failed
            else -> null
        }
    }

    private fun getNetworkErrorResId(code: Int): Int? {
        return when (code) {
            ErrorCodes.NO_INTERNET -> R.string.error_no_internet
            ErrorCodes.CONNECTION_TIMEOUT -> R.string.error_connection_timeout
            ErrorCodes.SERVER_UNAVAILABLE -> R.string.error_server_unavailable
            else -> null
        }
    }
}