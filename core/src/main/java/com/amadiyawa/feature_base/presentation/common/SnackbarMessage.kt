package com.amadiyawa.feature_base.presentation.common

/**
 * Common data structure for snackbar messages
 */
data class SnackbarMessage(
    val message: String,
    val isError: Boolean = false,
    val duration: Long? = null,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)