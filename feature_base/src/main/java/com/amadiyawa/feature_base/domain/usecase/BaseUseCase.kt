package com.amadiyawa.feature_base.domain.usecase

import com.amadiyawa.feature_base.domain.repository.ErrorLocalizer
import com.amadiyawa.feature_base.domain.result.OperationResult
import java.io.IOException

abstract class BaseUseCase<T>(
    protected val errorLocalizer: ErrorLocalizer
) {
    companion object {
        const val NETWORK_ERROR_CODE = 503
        const val SERVER_ERROR_CODE = 500

        private const val DEFAULT_NETWORK_ERROR = "Network unavailable"
        private const val DEFAULT_SERVER_ERROR = "System error occurred"
    }

    protected fun handleSuccess(response: T): OperationResult<T> {
        return OperationResult.success(response)
    }

    protected fun handleFailure(
        failure: OperationResult.Failure,
        defaultMessage: String = "Operation failed"
    ): OperationResult.Failure {
        val localizedMessage = failure.code?.let { code ->
            errorLocalizer.getLocalizedMessage(code, failure.message ?: defaultMessage)
        } ?: failure.message ?: defaultMessage

        return OperationResult.failure(
            code = failure.code,
            message = localizedMessage,
            details = failure.details
        )
    }

    protected fun handleError(
        error: OperationResult.Error,
        defaultMessage: String = DEFAULT_SERVER_ERROR
    ): OperationResult.Error {
        val (errorCode, baseMessage) = when (error.throwable) {
            is IOException -> NETWORK_ERROR_CODE to DEFAULT_NETWORK_ERROR
            else -> SERVER_ERROR_CODE to defaultMessage
        }

        return OperationResult.error(
            throwable = error.throwable,
            message = errorLocalizer.getLocalizedMessage(
                errorCode,
                error.message ?: baseMessage
            )
        )
    }
}