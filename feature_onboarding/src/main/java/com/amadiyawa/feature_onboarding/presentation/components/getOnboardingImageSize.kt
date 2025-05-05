package com.amadiyawa.feature_onboarding.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize
import com.amadiyawa.feature_base.presentation.theme.AppDimensions
import com.amadiyawa.feature_base.presentation.theme.dimension

/**
 * Calculates and returns the appropriate onboarding image size based on device orientation,
 * device type and size preference.
 *
 * @param isLarge Determines if a larger version of the image should be used. Defaults to false.
 * @return [IntSize] The calculated image dimensions as width/height in pixels.
 */
@Composable
fun getOnboardingImageSize(isLarge: Boolean = false): IntSize {
    val dimension = MaterialTheme.dimension
    return if (dimension.orientation == AppDimensions.Orientation.LANDSCAPE) {
        getLandscapeSize(isLarge, dimension.deviceType)
    } else {
        getPortraitSize(isLarge, dimension.deviceType)
    }
}

/**
 * Calculates image dimensions for landscape orientation.
 *
 * @param isLarge Flag for larger image variant
 * @param deviceType Type of device (phone/tablet)
 * @return [IntSize] Image dimensions for landscape mode:
 *         - Phone: 180x360 (large) or 150x300 (normal)
 *         - Tablet: 300x500 (large) or 250x450 (normal)
 */
@Composable
private fun getLandscapeSize(isLarge: Boolean, deviceType: AppDimensions.DeviceType): IntSize {
    return if (deviceType == AppDimensions.DeviceType.PHONE) {
        if (isLarge) IntSize(180, 360) else IntSize(150, 300)
    } else {
        if (isLarge) IntSize(300, 500) else IntSize(250, 450)
    }
}

/**
 * Calculates image dimensions for portrait orientation.
 *
 * @param isLarge Flag for larger image variant
 * @param deviceType Type of device (phone/tablet/large tablet)
 * @return [IntSize] Image dimensions for portrait mode:
 *         - Large Tablet: 720x480 (large) or 600x400 (normal)
 *         - Tablet: 640x450 (large) or 600x400 (normal)
 *         - Phone: 360x300 (large) or 360x240 (normal)
 */
@Composable
private fun getPortraitSize(isLarge: Boolean, deviceType: AppDimensions.DeviceType): IntSize {
    return when (deviceType) {
        AppDimensions.DeviceType.LARGE_TABLET -> {
            if (isLarge) IntSize(720, 480) else IntSize(600, 400)
        }
        AppDimensions.DeviceType.TABLET -> {
            if (isLarge) IntSize(640, 450) else IntSize(600, 400)
        }
        else -> {
            if (isLarge) IntSize(360, 300) else IntSize(360, 240)
        }
    }
}