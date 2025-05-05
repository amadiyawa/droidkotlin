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

sealed class TrailingIconConfig {
    data object None : TrailingIconConfig()
    data class Clearable(val text: String) : TrailingIconConfig()
    data class Password(
        val text: String,
        val isVisible: Boolean
    ) : TrailingIconConfig()
}

@Composable
fun TextFieldTrailingIcon(
    modifier: Modifier = Modifier,
    config: TrailingIconConfig,
    onVisibilityChange: () -> Unit = {},
    onClearText: () -> Unit = {}
) {
    when (config) {
        is TrailingIconConfig.None -> Unit
        is TrailingIconConfig.Clearable -> {
            if (config.text.isNotEmpty()) {
                ClearTextIcon(
                    onClick = onClearText,
                    modifier = modifier
                )
            }
        }
        is TrailingIconConfig.Password -> {
            PasswordVisibilityIcon(
                isVisible = config.isVisible,
                onClick = onVisibilityChange,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ClearTextIcon(
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(Dimen.Size.large)
    ) {
        Icon(
            imageVector = Icons.Filled.Cancel,
            contentDescription = stringResource(R.string.clear_text),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimen.Size.small)
        )
    }
}

@Composable
private fun PasswordVisibilityIcon(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(Dimen.Size.large)
    ) {
        Icon(
            imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            contentDescription = if (isVisible) {
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            },
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimen.Size.small)
        )
    }
}