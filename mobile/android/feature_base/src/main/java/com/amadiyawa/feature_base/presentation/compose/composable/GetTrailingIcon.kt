package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amadiyawa.feature_base.common.res.Dimen

data class TrailingIconConfig(
    var text: String = "",
    val isPasswordField: Boolean = false,
    val isPasswordVisible: Boolean = false
)

@Composable
fun GetTrailingIcon(
    config: TrailingIconConfig = TrailingIconConfig(),
    onVisibilityChange: () -> Unit = {},
    onClearText: () -> Unit
) {
    if (config.text.isNotEmpty() || config.isPasswordField) {
        IconButton(
            modifier = Modifier.size(Dimen.Size.large),
            onClick = {
                if (config.isPasswordField) {
                    onVisibilityChange()
                } else {
                    onClearText()
                }
            }
        ) {
            Icon(
                imageVector = if (config.isPasswordField) {
                    if (config.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Cancel
                },
                contentDescription = "",
                modifier = Modifier.size(Dimen.Size.small),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}