package com.amadiyawa.feature_base.presentation.screen.viewmodel

/**
 * Base UI events that can be emitted from any ViewModel
 */
sealed class BaseUiEvent {
    data class ShowSnackbar(
        val message: String,
        val isError: Boolean = false,
        val actionText: String? = null,
        val action: (() -> Unit)? = null
    ) : BaseUiEvent()

    object NavigateToSignIn : BaseUiEvent()

    data class ShowDialog(
        val title: String,
        val message: String,
        val positiveButton: String = "OK",
        val negativeButton: String? = null,
        val onPositiveClick: (() -> Unit)? = null,
        val onNegativeClick: (() -> Unit)? = null
    ) : BaseUiEvent()
}