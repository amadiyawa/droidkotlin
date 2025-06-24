package com.amadiyawa.feature_base.common.util

import androidx.compose.ui.graphics.Color

/**
 * Extension function to convert a hex color string to a Color object.
 *
 * @receiver String The hex color string to be converted. It should be in the format "#RRGGBB" or "#AARRGGBB".
 * @return Color The Color object representing the hex color string.
 * @throws IllegalArgumentException If the string cannot be parsed as a color.
 */
fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}