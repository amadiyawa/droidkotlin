package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.paygo.base.R

/**
 * Configuration for the trailing icon in a text field.
 *
 * @property text The current text in the field.
 * @property isPasswordField Indicates whether the field is a password field.
 * @property isPasswordVisible Indicates whether the password is visible.
 */
data class TrailingIconConfig(
    var text: String = "",
    val isPasswordField: Boolean = false,
    val isPasswordVisible: Boolean = false
)

/**
 * Composable function to display a trailing icon in a text field.
 *
 * This function displays an icon based on the configuration provided.
 * If the field is a password field, it toggles the visibility of the password.
 * Otherwise, it provides an option to clear the text.
 *
 * @param config The configuration for the trailing icon.
 * @param onVisibilityChange Callback triggered when the password visibility changes.
 * @param onClearText Callback triggered when the text is cleared.
 */
@Composable
fun GetTrailingIcon(
    config: TrailingIconConfig = TrailingIconConfig(),
    onVisibilityChange: () -> Unit = {},
    onClearText: () -> Unit
) {
    if (shouldShowIcon(config)) {
        IconButton(
            modifier = Modifier.size(Dimen.Size.large),
            onClick = { handleIconClick(config, onVisibilityChange, onClearText) }
        ) {
            Icon(
                imageVector = getImageVector(config),
                contentDescription = getContentDescription(config),
                modifier = Modifier.size(Dimen.Size.small),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun shouldShowIcon(config: TrailingIconConfig): Boolean {
    return config.text.isNotEmpty() || config.isPasswordField
}

private fun handleIconClick(
    config: TrailingIconConfig,
    onVisibilityChange: () -> Unit,
    onClearText: () -> Unit
) {
    if (config.isPasswordField) {
        onVisibilityChange()
    } else {
        onClearText()
    }
}

@Composable
private fun getImageVector(config: TrailingIconConfig) = when {
    config.isPasswordField -> if (config.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    else -> Icons.Filled.Cancel
}

@Composable
private fun getContentDescription(config: TrailingIconConfig): String {
    return when {
        config.isPasswordField -> if (config.isPasswordVisible) {
            stringResource(id = R.string.hide_password)
        } else {
            stringResource(id = R.string.show_password)
        }
        else -> stringResource(id = R.string.clear_text)
    }
}