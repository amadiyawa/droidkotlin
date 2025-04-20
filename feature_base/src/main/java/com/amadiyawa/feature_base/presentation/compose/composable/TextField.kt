package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.theme.dimension

data class TextFieldText(
    val value: String,
    val label: String,
    val placeholder: String = "",
    val errorMessage: String? = null
)

data class TextFieldConfig(
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val keyboardActions: KeyboardActions = KeyboardActions.Default,
    val height: Dp = Dimen.Size.extraLarge,
    val trailingIconConfig: TrailingIconConfig = TrailingIconConfig(),
    val visualTransformation: VisualTransformation = VisualTransformation.None
)

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    text: TextFieldText,
    onValueChange: (String) -> Unit,
    onClearText: () -> Unit,
    onVisibilityChange: () -> Unit = {},
    config: TextFieldConfig = TextFieldConfig(),
    leadingBadge: String? = null
) {
    config.trailingIconConfig.text = text.value
    Column(verticalArrangement = Arrangement.spacedBy(Dimen.Spacing.medium)) {
        TextLabelLarge(text = text.label)

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .requiredHeight(config.height)
                .border(
                    width = Dimen.Spacing.extraSmall,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(Dimen.Size.small)
                ),
            shape = RoundedCornerShape(Dimen.Size.small),
            trailingIcon = {
                GetTrailingIcon(
                    config = config.trailingIconConfig,
                    onClearText = onClearText,
                    onVisibilityChange = onVisibilityChange
                )
            },
            leadingIcon = leadingBadge?.takeIf { it.isNotBlank() }?.let {
                {
                    Box(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimension.gridOneAndHalf)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(Dimen.Size.extraSmall)
                            )
                            .padding(
                                horizontal = MaterialTheme.dimension.gridOne,
                                vertical = MaterialTheme.dimension.gridHalf
                            )
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            value = text.value,
            placeholder = { Text(text.placeholder) },
            onValueChange = onValueChange,
            keyboardOptions = config.keyboardOptions,
            keyboardActions = config.keyboardActions,
            visualTransformation = config.visualTransformation,
            colors = getTextFieldColors()
        )

        AnimatedVisibility(text.errorMessage != null) {
            text.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun getTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )
}

@Preview
@Composable
fun DefaultTextFieldPreview() {
    DefaultTextField(
        text = TextFieldText(value = "", label = "Phone Number"),
        onClearText = { },
        onValueChange = { }
    )
}