package com.amadiyawa.feature_base.data.repository

import android.content.Context
import com.amadiyawa.droidkotlin.base.R
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

class AndroidErrorLocalizer(
    private val context: Context
) : ErrorLocalizer {

    // Thread-safe cache for better performance
    private val localizedMessageCache = ConcurrentHashMap<Pair<Int, String>, String>()

    override fun getLocalizedMessage(errorCode: Int, defaultMessage: String): String {
        val cacheKey = errorCode to defaultMessage

        return localizedMessageCache.getOrPut(cacheKey) {
            getLocalizedMessageInternal(errorCode, defaultMessage)
        }
    }

    private fun getLocalizedMessageInternal(errorCode: Int, defaultMessage: String): String {
        return when (val knownError = ErrorCode.fromCode(errorCode)) {
            null -> defaultMessage.ifBlank { getDefaultErrorMessage() }
            else -> {
                try {
                    val localizedMessage = context.getString(knownError.resId)
                    if (localizedMessage.isNotBlank()) {
                        localizedMessage
                    } else {
                        Timber.w("Empty localized string for error code $errorCode")
                        defaultMessage.ifBlank { getDefaultErrorMessage() }
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Failed to get localized string for error code $errorCode")
                    defaultMessage.ifBlank { getDefaultErrorMessage() }
                }
            }
        }
    }

    // Use localized default messages
    private fun getDefaultErrorMessage(): String {
        return try {
            context.getString(R.string.error_general)
        } catch (e: Exception) {
            "Error" // Ultimate fallback
        }
    }

    private fun getDefaultNetworkErrorMessage(): String {
        return try {
            context.getString(R.string.error_network_general)
        } catch (e: Exception) {
            "Network error" // Ultimate fallback
        }
    }

    private fun getDefaultServerErrorMessage(): String {
        return try {
            context.getString(R.string.error_server_general)
        } catch (e: Exception) {
            "Server error" // Ultimate fallback
        }
    }

    fun clearCache() {
        localizedMessageCache.clear()
    }

    sealed class ErrorCode(val code: Int, val resId: Int) {
        // HTTP Error Codes (4xx - Client Errors)
        object BadRequest : ErrorCode(400, R.string.error_400)
        object Unauthorized : ErrorCode(401, R.string.error_401)
        object Forbidden : ErrorCode(403, R.string.error_403)
        object NotFound : ErrorCode(404, R.string.error_404)
        object MethodNotAllowed : ErrorCode(405, R.string.error_405)
        object RequestTimeout : ErrorCode(408, R.string.error_408)
        object TooManyRequests : ErrorCode(429, R.string.error_429)

        // HTTP Error Codes (5xx - Server Errors)
        object InternalServerError : ErrorCode(500, R.string.error_500)
        object BadGateway : ErrorCode(502, R.string.error_502)
        object ServiceUnavailable : ErrorCode(503, R.string.error_503)
        object GatewayTimeout : ErrorCode(504, R.string.error_504)

        // Network Errors (7xx - Custom Network Errors)
        object NetworkUnavailable : ErrorCode(700, R.string.error_network_unavailable)
        object ConnectionTimeout : ErrorCode(701, R.string.error_connection_timeout)
        object HostUnreachable : ErrorCode(702, R.string.error_host_unreachable)

        // Application Errors (1xxx - Session)
        object SessionSaveError : ErrorCode(1000, R.string.error_session_save_user)
        object SessionGetError : ErrorCode(1001, R.string.error_session_get_user)
        object SessionUpdateError : ErrorCode(1002, R.string.error_session_update_state)
        object SessionClearError : ErrorCode(1003, R.string.error_session_clear)
        object SessionExpired : ErrorCode(1004, R.string.error_session_expired)
        object SessionInvalid : ErrorCode(1005, R.string.error_session_invalid)

        // Application Errors (2xxx - Authentication)
        object AuthenticationFailed : ErrorCode(2000, R.string.error_authentication_failed)
        object InvalidCredentials : ErrorCode(2001, R.string.error_invalid_credentials)
        object AccountLocked : ErrorCode(2002, R.string.error_account_locked)
        object AccountNotVerified : ErrorCode(2003, R.string.error_account_not_verified)
        object TokenExpired : ErrorCode(2004, R.string.error_token_expired)
        object TokenInvalid : ErrorCode(2005, R.string.error_token_invalid)

        // Application Errors (3xxx - Validation)
        object ValidationError : ErrorCode(3000, R.string.error_validation)
        object RequiredFieldMissing : ErrorCode(3001, R.string.error_required_field_missing)
        object InvalidEmailFormat : ErrorCode(3002, R.string.error_invalid_email_format)
        object InvalidPhoneFormat : ErrorCode(3003, R.string.error_invalid_phone_format)
        object PasswordTooWeak : ErrorCode(3004, R.string.error_password_too_weak)

        // Application Errors (4xxx - Profile/User)
        object ProfileUpdateFailed : ErrorCode(4000, R.string.error_profile_update_failed)
        object ProfileNotFound : ErrorCode(4001, R.string.error_profile_not_found)
        object AvatarUploadFailed : ErrorCode(4002, R.string.error_avatar_upload_failed)

        companion object {
            private val codeMap: Map<Int, ErrorCode> by lazy {
                ErrorCode::class.sealedSubclasses
                    .mapNotNull { it.objectInstance }
                    .associateBy { it.code }
            }

            fun fromCode(code: Int): ErrorCode? = codeMap[code]
            fun getAllCodes(): List<ErrorCode> = codeMap.values.toList()
        }
    }
}