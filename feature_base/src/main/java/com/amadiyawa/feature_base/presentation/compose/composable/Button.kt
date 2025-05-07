package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.common.resources.Dimen
import com.amadiyawa.feature_base.presentation.theme.dimension

data class LoadingButtonParams(
    val modifier: Modifier = Modifier,
    val enabled: Boolean = true,
    var text: String = "",
    var isLoading: Boolean = false,
    var onClick: () -> Unit = {}
)

@Composable
fun LoadingButton(
    params: LoadingButtonParams,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    )
) {
    Button(
        modifier = params.modifier
            .fillMaxWidth()
            .requiredHeight(MaterialTheme.dimension.componentSize.inputField),
        onClick = params.onClick,
        enabled = params.enabled && !params.isLoading,
        colors = colors
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = params.isLoading,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                DefaultCircularProgressIndicator()
            }
            TextLabelLarge(
                text = params.text,
                fontWeight = FontWeight.Medium,
                color = LocalContentColor.current
            )
        }
    }
}

@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled,
        colors = colors
    ) {
        Text(text = text)
    }
}

@Composable
fun AppOutlinedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f)
    )
) {
    OutlinedButton(
        modifier = modifier
            .border(
                width = Dimen.Spacing.extraSmall,
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(Dimen.Size.medium)
            ),
        onClick = { onClick() },
        enabled = enabled,
        colors = colors
    ) {
        Text(text = text)
    }
}

/**
 * A composable function that creates a text button.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The action to be performed when the button is clicked.
 * @param text The text to be displayed on the button.
 * @param color The color of the text. Defaults to the primary color in the MaterialTheme.
 * @param textDecoration The text decoration to be applied to the text. Defaults to none.
 * @param enabled Indicates whether the button is enabled. Defaults to true.
 */
@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    textDecoration: TextDecoration = TextDecoration.None,
    enabled: Boolean = true
) {
    Text(
        text = text,
        modifier = if (enabled) {
            modifier.clickable { onClick.invoke() }
        } else {
            modifier
        },
        color = if (enabled) color else MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyLarge.copy(
            textDecoration = textDecoration
        )
    )
}

sealed class ButtonIconType {
    data class Resource(val resId: Int) : ButtonIconType()
    data class Vector(val imageVector: ImageVector) : ButtonIconType()
}

data class CircularButtonParams(
    var onClick: () -> Unit = {},
    var enabled: Boolean = true,
    var iconType: ButtonIconType,
    var description: String = "",
    var size: Dp = 48.dp,
    var iconSize: Dp = 24.dp,
    var backgroundColor: Color = Color.Transparent,
    var iconTint: Color = Color.Unspecified,
    var borderWidth: Dp = 0.dp,
    var borderColor: Color = Color.Transparent
)

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    params: CircularButtonParams
) {
    Box(
        modifier = modifier
            .size(params.size)
            .clip(CircleShape)
            .border(
                width = params.borderWidth,
                color = params.borderColor,
                shape = CircleShape
            )
            .background(color = params.backgroundColor, shape = CircleShape)
            .clickable(
                enabled = params.enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = params.onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        when (val iconType = params.iconType) {
            is ButtonIconType.Resource -> {
                Icon(
                    painter = painterResource(id = iconType.resId),
                    contentDescription = params.description,
                    tint = params.iconTint,
                    modifier = Modifier.size(params.iconSize)
                )
            }
            is ButtonIconType.Vector -> {
                Icon(
                    imageVector = iconType.imageVector,
                    contentDescription = params.description,
                    tint = params.iconTint,
                    modifier = Modifier.size(params.iconSize)
                )
            }
        }
    }
}