package com.amadiyawa.feature_base.presentation.compose.composable

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
import androidx.compose.ui.Modifier
import com.amadiyawa.feature_base.presentation.theme.dimension

/**
 * A composable function that displays a progression indicator with multiple levels.
 *
 * @param currentLevel The current level of progression.
 * @param totalLevels The total number of levels in the progression.
 *
 * @author Amadou Iyawa
 */
@Composable
fun ProgressionIndicator(currentLevel: Int, totalLevels: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (level in 0..totalLevels) {
            ProgressRectangle(
                isActive = level <= currentLevel,
                modifier = Modifier
                    .weight(1f)
                    .then(if (level != totalLevels) Modifier.padding(end = MaterialTheme.dimension.gridOne) else Modifier)
            )
        }
    }
}

/**
 * A composable function that represents a rectangular progress indicator.
 *
 * @param isActive A boolean indicating whether the progress rectangle is active or not.
 * @param modifier A [Modifier] to be applied to the progress rectangle.
 *
 * @author Amadou Iyawa
 */
@Composable
fun ProgressRectangle(isActive: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(MaterialTheme.dimension.gridOne)
            .background(
                color = getActiveColor(isActive),
                shape = RoundedCornerShape(MaterialTheme.dimension.gridHalf)
            )
    )
}