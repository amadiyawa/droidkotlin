package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.amadiyawa.feature_base.common.res.Dimen

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
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(Dimen.Size.extraLarge),
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f)
    )
) {
    OutlinedButton(
        modifier = modifier
            .border(
                width = Dimen.Spacing.extraSmall,
                color = Color.White,
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
 * TextButton
 */
@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    style: TextButtonStyle = TextButtonStyle()
    ) {
    TextBodySmall(
        text = text,
        modifier = modifier.clickable { onClick() },
        color = color,
        textAlign = style.textAlign,
        textDecoration = style.textDecoration,
        fontWeight = style.fontWeight,
        fontSize = style.fontSize
    )
}

