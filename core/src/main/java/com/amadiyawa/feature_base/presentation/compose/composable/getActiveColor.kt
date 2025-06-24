package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Returns the active color based on the given state.
 *
 * This function checks if the provided state is active and returns the primary color
 * from the Material theme if true, otherwise returns gray.
 *
 * @param isActive A boolean indicating whether the state is active.
 * @return The color corresponding to the active state.
 */
@Composable
fun getActiveColor(isActive: Boolean): Color {
    return if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
}