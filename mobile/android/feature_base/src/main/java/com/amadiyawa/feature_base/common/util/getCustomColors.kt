package com.amadiyawa.feature_base.common.util

import com.amadiyawa.feature_base.presentation.theme.CustomColor
import com.amadiyawa.feature_base.presentation.theme.CustomDarkColor
import com.amadiyawa.feature_base.presentation.theme.CustomLightColor

fun getCustomColors(darkTheme: Boolean): CustomColor {
    return if (darkTheme) {
        CustomDarkColor()
    } else {
        CustomLightColor()
    }
}