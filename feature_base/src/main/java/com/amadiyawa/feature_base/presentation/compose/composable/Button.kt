package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.theme.dimension

data class TextButtonStyle(
    val textAlign: TextAlign = TextAlign.Start,
    val textDecoration: TextDecoration = TextDecoration.None,
    val fontWeight: FontWeight = FontWeight.Normal,
    val fontSize: TextUnit = TextUnit.Unspecified,
)

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
 */
@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    textDecoration: TextDecoration = TextDecoration.None
) {
    Text(
        text = text,
        modifier = modifier.clickable { onClick.invoke() },
        color = color,
        style = MaterialTheme.typography.bodyLarge.copy(
            textDecoration = textDecoration
        )
    )
}

/**
 * Data class representing the parameters for a circular button.
 *
 * @property onClick The action to be performed when the button is clicked.
 * @property enabled Indicates whether the button is enabled.
 * @property imageVector The image vector for the icon displayed on the button.
 * @property description The content description for the icon, used for accessibility.
 */
data class CircularButtonParams(
    val onClick: () -> Unit,
    val enabled: Boolean,
    val imageVector: ImageVector,
    val description: String = ""
)

/**
 * A composable function that creates a circular button with an icon.
 *
 * @param modifier The modifier to be applied to the button.
 * @param circularButtonParams The parameters for the circular button, including the onClick action,
 *                             whether the button is enabled, the image vector for the icon, and the description.
 * @param size The size of the button. Defaults to the grid size defined in the MaterialTheme.
 * @param color The background color of the button. Defaults to the primary color in the MaterialTheme.
 * @param onColor The color of the icon. Defaults to the onPrimary color in the MaterialTheme.
 */
@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    circularButtonParams: CircularButtonParams,
    size: Dp = MaterialTheme.dimension.gridSix,
    color: Color = MaterialTheme.colorScheme.primary,
    onColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    IconButton(
        onClick = circularButtonParams.onClick,
        enabled = circularButtonParams.enabled,
        modifier = modifier
            .requiredSize(size)
            .background(color = color, shape = CircleShape)
    ) {
        Icon(
            imageVector = circularButtonParams.imageVector,
            contentDescription = circularButtonParams.description,
            tint = onColor
        )
    }
}

@Composable
fun SocialButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    description: String = "",
    onClick: () -> Unit,
    size: Dp = MaterialTheme.dimension.gridSix,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(size)
    ) {
        Icon(
            painter = painter,
            contentDescription = description,
            tint = Color.Unspecified
        )
    }
}