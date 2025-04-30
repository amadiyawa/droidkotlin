package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.presentation.theme.dimension

/**
 * A composable that displays a horizontal progress indicator made of bars.
 * Each bar represents a level, with animation and visual emphasis to indicate
 * the current level and completed levels.
 *
 * @param currentLevel The current level (0-based). All levels up to and including
 *                    this value will be displayed as active with special animation.
 * @param totalLevels The total number of levels to display (0-based). Determines
 *                    the total number of bars in the indicator.
 *
 * The bars have the following characteristics:
 * - Completed levels are highlighted
 * - Current level has scale animation and different height
 * - Inactive bars have a muted appearance
 * - Each bar smoothly animates during transitions
 */
@Composable
fun ProgressionIndicator(currentLevel: Int, totalLevels: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (level in 0..totalLevels) {
            val isActive = level <= currentLevel
            val isCurrent = level == currentLevel

            // Animate the color change
            val color by animateColorAsState(
                targetValue = getActiveColor(isActive),
                animationSpec = tween(durationMillis = 500),
                label = "color"
            )

            // Create a small animation delay based on position
            val animDelay = 50 * level

            // Animate the weight multiplier
            val weightMultiplier by animateFloatAsState(
                targetValue = if (isActive) 1.5f else 1f,
                // Use tween with delay instead of spring with delay
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = animDelay,
                    easing = FastOutSlowInEasing
                ),
                label = "weight"
            )

            // Optional: height animation for current indicator
            val height by animateDpAsState(
                targetValue = if (isCurrent) 5.dp else MaterialTheme.dimension.grid.single,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "height"
            )

            Box(
                modifier = Modifier
                    .weight(weightMultiplier)
                    .padding(horizontal = MaterialTheme.dimension.spacing.xSmall)
                    .height(height)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(MaterialTheme.dimension.radius.tiny)
                    )
                    // Optional subtle scale animation for current indicator
                    .then(
                        if (isCurrent) {
                            val scale by animateFloatAsState(
                                targetValue = 1.05f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "scale"
                            )
                            Modifier.scale(scale)
                        } else Modifier
                    )
            )
        }
    }
}