package com.amadiyawa.feature_base.domain.mapper

import com.amadiyawa.feature_base.domain.util.ErrorCategory

interface ErrorMessageMapper {
    /**
     * Maps an error code to a localized user-friendly message
     *
     * @param code The error code from OperationResult.Failure
     * @param defaultMessageResId Resource ID for default message to use if no mapping exists
     * @param category Optional error category to narrow down the search
     * @return User-friendly error message
     */
    fun getMessage(code: Int?, defaultMessageResId: Int, category: ErrorCategory? = null): String
}