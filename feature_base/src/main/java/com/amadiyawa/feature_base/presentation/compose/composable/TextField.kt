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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.amadiyawa.feature_base.common.resources.Dimen
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
    val trailingIconConfig: TrailingIconConfig,
    val visualTransformation: VisualTransformation = VisualTransformation.None
)

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    text: TextFieldText,
    onValueChange: (String) -> Unit,
    onClearText: () -> Unit = {},
    onVisibilityChange: () -> Unit = {},
    config: TextFieldConfig = TextFieldConfig(
        trailingIconConfig = TrailingIconConfig.None
    ),
    leadingBadge: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    // Create an updated trailing icon config based on the current text value
    val updatedTrailingIconConfig = when (val iconConfig = config.trailingIconConfig) {
        is TrailingIconConfig.Clearable -> iconConfig.copy(text = text.value)
        is TrailingIconConfig.Password -> iconConfig.copy(text = text.value)
        TrailingIconConfig.None -> iconConfig
    }

    val updatedConfig = config.copy(trailingIconConfig = updatedTrailingIconConfig)

    val (interactionSource, animatedBorderWidth, animatedBorderColor) = rememberAnimatedBorderStyle(
        isError = text.errorMessage != null
    )

    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.spacing.small)) {
        TextLabelLarge(text = text.label)

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .requiredHeight(updatedConfig.height)
                .border(
                    width = animatedBorderWidth,
                    color = animatedBorderColor,
                    shape = RoundedCornerShape(Dimen.Size.small)
                ),
            interactionSource = interactionSource,
            shape = RoundedCornerShape(Dimen.Size.small),
            trailingIcon = {
                TextFieldTrailingIcon(
                    config = updatedConfig.trailingIconConfig,
                    onClearText = onClearText,
                    onVisibilityChange = onVisibilityChange
                )
            },
            leadingIcon = leadingIcon ?: leadingBadge?.takeIf { it.isNotBlank() }?.let {
                {
                    Box(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimension.spacing.small)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(Dimen.Size.extraSmall)
                            )
                            .padding(
                                horizontal = MaterialTheme.dimension.spacing.small,
                                vertical = MaterialTheme.dimension.spacing.xSmall
                            )
                    ) {
                        TextLabelMedium(
                            text = it,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            value = text.value,
            placeholder = { Text(text.placeholder) },
            onValueChange = onValueChange,
            keyboardOptions = updatedConfig.keyboardOptions,
            keyboardActions = updatedConfig.keyboardActions,
            visualTransformation = updatedConfig.visualTransformation,
            colors = getTextFieldColors()
        )

        AnimatedVisibility(text.errorMessage != null) {
            text.errorMessage?.let {
                TextBodySmall(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
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