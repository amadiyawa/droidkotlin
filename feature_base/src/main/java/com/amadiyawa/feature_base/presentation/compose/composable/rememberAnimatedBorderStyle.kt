package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.amadiyawa.feature_base.common.res.Dimen

@Composable
fun rememberAnimatedBorderStyle(
    isError: Boolean
): Triple<MutableInteractionSource, Dp, Color> {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    val borderWidth = when {
        isError -> Dimen.Spacing.almostSmall
        isFocused -> Dimen.Spacing.almostSmall
        else -> Dimen.Spacing.extraSmall
    }

    val animatedBorderColor by animateColorAsState(
        targetValue = borderColor,
        label = "borderColorAnim"
    )
    val animatedBorderWidth by animateDpAsState(
        targetValue = borderWidth,
        label = "borderWidthAnim"
    )

    return Triple(interactionSource, animatedBorderWidth, animatedBorderColor)
}