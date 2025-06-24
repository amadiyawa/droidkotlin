package com.amadiyawa.feature_base.common.util

import com.amadiyawa.feature_base.presentation.theme.CustomColor
import com.amadiyawa.feature_base.presentation.theme.CustomDarkColor
import com.amadiyawa.feature_base.presentation.theme.CustomLightColor

/**
 * Retrieves the custom colors based on the theme.
 *
 * @param darkTheme A boolean indicating whether the dark theme is enabled.
 * @return A CustomColor object representing the appropriate colors for the theme.
 */
fun getCustomColors(darkTheme: Boolean): CustomColor {
    return if (darkTheme) {
        CustomDarkColor()
    } else {
        CustomLightColor()
    }
}