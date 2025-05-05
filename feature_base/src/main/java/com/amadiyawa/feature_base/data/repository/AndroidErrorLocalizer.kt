package com.amadiyawa.feature_base.data.repository

import android.content.Context
import android.content.res.Resources
import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.paygo.base.R
import timber.log.Timber

class AndroidErrorLocalizer(
    private val context: Context
) : ErrorLocalizer {

    // Define error codes as sealed interface for type safety
    sealed interface ErrorCode {
        val code: Int
        val resId: Int

        // Common HTTP errors
        object BadRequest : ErrorCode {
            override val code = 400
            override val resId = R.string.error_400
        }
        object Unauthorized : ErrorCode {
            override val code = 401
            override val resId = R.string.error_401
        }
        object Forbidden : ErrorCode {
            override val code = 403
            override val resId = R.string.error_403
        }
        object InternalError : ErrorCode {
            override val code = 500
            override val resId = R.string.error_500
        }
        object ServiceUnavailable : ErrorCode {
            override val code = 503
            override val resId = R.string.error_503
        }

        // Session Errors
        object SessionSaveError : ErrorCode {
            override val code = 1000
            override val resId = R.string.error_session_save_user
        }
        object SessionGetError : ErrorCode {
            override val code = 1001
            override val resId = R.string.error_session_get_user
        }
        object SessionUpdateError : ErrorCode {
            override val code = 1002
            override val resId = R.string.error_session_update_state
        }
        object SessionClearError : ErrorCode {
            override val code = 1003
            override val resId = R.string.error_session_clear
        }
        object SessionGenericError : ErrorCode {
            override val code = 1099
            override val resId = R.string.error_session_generic
        }
    }

    // Cache for better performance
    private val errorCodeMap = mutableMapOf<Int, ErrorCode>().apply {
        listOf(
            // Common HTTP errors
            ErrorCode.BadRequest,
            ErrorCode.Unauthorized,
            ErrorCode.Forbidden,
            ErrorCode.InternalError,
            ErrorCode.ServiceUnavailable,

            // Session Errors
            ErrorCode.SessionSaveError,
            ErrorCode.SessionGetError,
            ErrorCode.SessionUpdateError,
            ErrorCode.SessionClearError,
            ErrorCode.SessionGenericError
        ).forEach { put(it.code, it) }
    }

    override fun getLocalizedMessage(errorCode: Int, defaultMessage: String): String {
        return when (val knownError = errorCodeMap[errorCode]) {
            null -> defaultMessage
            else -> {
                try {
                    context.getString(knownError.resId).takeIf { it.isNotBlank() }
                        ?: defaultMessage
                } catch (e: Resources.NotFoundException) {
                    Timber.e(e, "Missing string resource for error code $errorCode")
                    defaultMessage
                }
            }
        }
    }
}