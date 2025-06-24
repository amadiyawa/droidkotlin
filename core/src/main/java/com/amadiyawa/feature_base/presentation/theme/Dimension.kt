package com.amadiyawa.feature_base.presentation.theme

import android.content.res.Configuration
import android.os.Build
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import timber.log.Timber

private const val TAG = "DeviceDimensions"

/**
 * A comprehensive dimension system that adapts to different screen sizes,
 * device orientations, and manufacturer implementations.
 */
object AppDimensions {
    /**
     * Device orientation enum
     */
    enum class Orientation {
        PORTRAIT,
        LANDSCAPE
    }

    /**
     * Grid dimensions for consistent spacing
     */
    data class Grid(
        val quarter: Dp = 1.dp,
        val half: Dp = 2.dp,
        val single: Dp = 4.dp,
        val double: Dp = 8.dp,
        val triple: Dp = 12.dp,
        val quadruple: Dp = 16.dp,
        val quintuple: Dp = 20.dp,
        val sextuple: Dp = 24.dp,
        val septuple: Dp = 28.dp,
        val octuple: Dp = 32.dp,
        val nonuple: Dp = 36.dp,
        val decuple: Dp = 40.dp,
        val undecuple: Dp = 44.dp,
        val duodecuple: Dp = 48.dp,
        val large: Dp = 64.dp,
        val extraLarge: Dp = 96.dp
    )

    /**
     * Spacing dimensions for padding and margins
     */
    data class Spacing(
        val none: Dp = 0.dp,
        val xxxSmall: Dp = 1.dp,
        val xxSmall: Dp = 2.dp,
        val xSmall: Dp = 4.dp,
        val small: Dp = 8.dp,
        val medium: Dp = 16.dp,
        val large: Dp = 24.dp,
        val xLarge: Dp = 32.dp,
        val xxLarge: Dp = 48.dp,
        val xxxLarge: Dp = 64.dp
    )

    /**
     * Component sizing dimensions
     */
    data class ComponentSize(
        val touchTarget: Dp = 48.dp,
        val iconTiny: Dp = 12.dp,
        val iconSmall: Dp = 16.dp,
        val iconMedium: Dp = 24.dp,
        val iconLarge: Dp = 32.dp,
        val iconExtraLarge: Dp = 48.dp,
        val button: Dp = 36.dp,
        val buttonLarge: Dp = 48.dp,
        val appBar: Dp = 64.dp,
        val bottomBar: Dp = 80.dp,
        val listItem: Dp = 56.dp,
        val listItemSmall: Dp = 48.dp,
        val listItemLarge: Dp = 72.dp,
        val inputField: Dp = 56.dp,
        val card: Dp = 120.dp,
        val dialog: Dp = 320.dp,
        val divider: Dp = 1.dp,
        // Added padding dimensions for common use cases
        val contentPadding: Dp = 16.dp,
        val screenEdgePadding: Dp = 16.dp,
        val itemSpacing: Dp = 8.dp
    )

    /**
     * Corner radius dimensions
     */
    data class Radius(
        val none: Dp = 0.dp,
        val tiny: Dp = 2.dp,
        val small: Dp = 4.dp,
        val medium: Dp = 8.dp,
        val large: Dp = 12.dp,
        val xLarge: Dp = 16.dp,
        val xxLarge: Dp = 24.dp,
        val circular: Dp = 1000.dp // Effectively circular for most components
    )

    /**
     * Elevation dimensions
     */
    data class Elevation(
        val none: Dp = 0.dp,
        val tiny: Dp = 1.dp,
        val small: Dp = 2.dp,
        val medium: Dp = 4.dp,
        val large: Dp = 8.dp,
        val xLarge: Dp = 16.dp
    )

    /**
     * Class to hold all dimension categories
     */
    data class Dimensions(
        val grid: Grid = Grid(),
        val spacing: Spacing = Spacing(),
        val componentSize: ComponentSize = ComponentSize(),
        val radius: Radius = Radius(),
        val elevation: Elevation = Elevation(),
        val orientation: Orientation = Orientation.PORTRAIT,
        val deviceType: DeviceType = DeviceType.PHONE
    ) {
        companion object {
            /**
             * Creates dimensions adjusted for a device
             * @param deviceType The device type to create dimensions for
             * @param orientation The current device orientation
             * @param pixelCorrection Apply Pixel-specific correction if needed
             */
            fun forDeviceType(
                deviceType: DeviceType = DeviceType.PHONE,
                orientation: Orientation = Orientation.PORTRAIT,
                pixelCorrection: Boolean = false
            ): Dimensions {
                // Base dimensions for the device type
                val baseDimensions = when (deviceType) {
                    DeviceType.PHONE -> Dimensions(
                        deviceType = deviceType,
                        orientation = orientation
                    )
                    DeviceType.TABLET -> Dimensions(
                        grid = Grid(
                            quarter = 1.5.dp,
                            half = 3.dp,
                            single = 6.dp,
                            double = 12.dp,
                            triple = 18.dp,
                            quadruple = 24.dp,
                            quintuple = 30.dp,
                            sextuple = 36.dp,
                            septuple = 42.dp,
                            octuple = 48.dp,
                            nonuple = 54.dp,
                            decuple = 60.dp,
                            undecuple = 66.dp,
                            duodecuple = 72.dp,
                            large = 96.dp,
                            extraLarge = 144.dp
                        ),
                        spacing = Spacing(
                            xxxSmall = 1.5.dp,
                            xxSmall = 3.dp,
                            xSmall = 6.dp,
                            small = 12.dp,
                            medium = 24.dp,
                            large = 36.dp,
                            xLarge = 48.dp,
                            xxLarge = 72.dp,
                            xxxLarge = 96.dp
                        ),
                        componentSize = ComponentSize(
                            touchTarget = 64.dp,
                            iconTiny = 18.dp,
                            iconSmall = 24.dp,
                            iconMedium = 32.dp,
                            iconLarge = 48.dp,
                            iconExtraLarge = 64.dp,
                            button = 48.dp,
                            buttonLarge = 64.dp,
                            appBar = 72.dp,
                            bottomBar = 96.dp,
                            listItem = 72.dp,
                            listItemSmall = 64.dp,
                            listItemLarge = 88.dp,
                            inputField = 72.dp,
                            card = 180.dp,
                            dialog = 400.dp,
                            divider = 1.dp,
                            contentPadding = 24.dp,
                            screenEdgePadding = 24.dp,
                            itemSpacing = 12.dp
                        ),
                        radius = Radius(
                            tiny = 3.dp,
                            small = 6.dp,
                            medium = 12.dp,
                            large = 18.dp,
                            xLarge = 24.dp,
                            xxLarge = 36.dp
                        ),
                        elevation = Elevation(
                            tiny = 1.5.dp,
                            small = 3.dp,
                            medium = 6.dp,
                            large = 12.dp,
                            xLarge = 24.dp
                        ),
                        deviceType = deviceType,
                        orientation = orientation
                    )
                    DeviceType.LARGE_TABLET -> Dimensions(
                        grid = Grid(
                            quarter = 2.dp,
                            half = 4.dp,
                            single = 8.dp,
                            double = 16.dp,
                            triple = 24.dp,
                            quadruple = 32.dp,
                            quintuple = 40.dp,
                            sextuple = 48.dp,
                            septuple = 56.dp,
                            octuple = 64.dp,
                            nonuple = 72.dp,
                            decuple = 80.dp,
                            undecuple = 88.dp,
                            duodecuple = 96.dp,
                            large = 128.dp,
                            extraLarge = 192.dp
                        ),
                        spacing = Spacing(
                            xxxSmall = 2.dp,
                            xxSmall = 4.dp,
                            xSmall = 8.dp,
                            small = 16.dp,
                            medium = 32.dp,
                            large = 48.dp,
                            xLarge = 64.dp,
                            xxLarge = 96.dp,
                            xxxLarge = 128.dp
                        ),
                        componentSize = ComponentSize(
                            touchTarget = 72.dp,
                            iconTiny = 24.dp,
                            iconSmall = 32.dp,
                            iconMedium = 48.dp,
                            iconLarge = 64.dp,
                            iconExtraLarge = 80.dp,
                            button = 56.dp,
                            buttonLarge = 72.dp,
                            appBar = 80.dp,
                            bottomBar = 112.dp,
                            listItem = 80.dp,
                            listItemSmall = 72.dp,
                            listItemLarge = 96.dp,
                            inputField = 80.dp,
                            card = 240.dp,
                            dialog = 480.dp,
                            divider = 2.dp,
                            contentPadding = 32.dp,
                            screenEdgePadding = 32.dp,
                            itemSpacing = 16.dp
                        ),
                        radius = Radius(
                            tiny = 4.dp,
                            small = 8.dp,
                            medium = 16.dp,
                            large = 24.dp,
                            xLarge = 32.dp,
                            xxLarge = 48.dp
                        ),
                        elevation = Elevation(
                            tiny = 2.dp,
                            small = 4.dp,
                            medium = 8.dp,
                            large = 16.dp,
                            xLarge = 32.dp
                        ),
                        deviceType = deviceType,
                        orientation = orientation
                    )
                }

                // Apply Pixel-specific correction if needed
                // The correction factor is higher for Pixel 8 Pro (1.25) compared to other Pixel devices (1.15)
                val pixelCorrectionFactor = if (pixelCorrection) {
                    // Pixel 8 Pro needs more correction than older Pixel devices
                    if (Build.MODEL.contains("Pixel 8 Pro", ignoreCase = true)) {
                        1.25f
                    } else {
                        1.15f
                    }
                } else {
                    1.0f
                }

                val correctedDimensions = if (pixelCorrectionFactor != 1.0f) {
                    baseDimensions.copy(
                        grid = baseDimensions.grid.scale(pixelCorrectionFactor),
                        spacing = baseDimensions.spacing.scale(pixelCorrectionFactor),
                        componentSize = baseDimensions.componentSize.scale(pixelCorrectionFactor),
                        radius = baseDimensions.radius.scale(pixelCorrectionFactor),
                        elevation = baseDimensions.elevation.scale(pixelCorrectionFactor)
                    )
                } else {
                    baseDimensions
                }

                // Apply orientation-specific adjustments
                return if (orientation == Orientation.LANDSCAPE) {
                    // In landscape, we typically want:
                    // 1. Shorter app bars and bottom bars
                    // 2. Wider dialogs and cards
                    // 3. Slightly smaller list items to fit more content
                    // 4. Different padding for screen edges
                    correctedDimensions.copy(
                        componentSize = correctedDimensions.componentSize.copy(
                            appBar = correctedDimensions.componentSize.appBar * 0.9f,
                            bottomBar = correctedDimensions.componentSize.bottomBar * 0.85f,
                            listItem = correctedDimensions.componentSize.listItem * 0.9f,
                            listItemSmall = correctedDimensions.componentSize.listItemSmall * 0.9f,
                            listItemLarge = correctedDimensions.componentSize.listItemLarge * 0.9f,
                            dialog = correctedDimensions.componentSize.dialog * 1.2f,
                            card = correctedDimensions.componentSize.card * 1.2f,
                            // Adjust screen edge padding for landscape
                            screenEdgePadding = correctedDimensions.componentSize.screenEdgePadding * 0.75f
                        )
                    )
                } else {
                    correctedDimensions
                }
            }
        }
    }

    /**
     * Device type enum to determine base dimensions
     */
    enum class DeviceType {
        PHONE,
        TABLET,
        LARGE_TABLET
    }
}

/**
 * Scale extension function for Grid
 */
fun AppDimensions.Grid.scale(factor: Float): AppDimensions.Grid = AppDimensions.Grid(
    quarter = (quarter.value * factor).dp,
    half = (half.value * factor).dp,
    single = (single.value * factor).dp,
    double = (double.value * factor).dp,
    triple = (triple.value * factor).dp,
    quadruple = (quadruple.value * factor).dp,
    quintuple = (quintuple.value * factor).dp,
    sextuple = (sextuple.value * factor).dp,
    septuple = (septuple.value * factor).dp,
    octuple = (octuple.value * factor).dp,
    nonuple = (nonuple.value * factor).dp,
    decuple = (decuple.value * factor).dp,
    undecuple = (undecuple.value * factor).dp,
    duodecuple = (duodecuple.value * factor).dp,
    large = (large.value * factor).dp,
    extraLarge = (extraLarge.value * factor).dp
)

/**
 * Scale extension function for Spacing
 */
fun AppDimensions.Spacing.scale(factor: Float): AppDimensions.Spacing = AppDimensions.Spacing(
    none = none,
    xxxSmall = (xxxSmall.value * factor).dp,
    xxSmall = (xxSmall.value * factor).dp,
    xSmall = (xSmall.value * factor).dp,
    small = (small.value * factor).dp,
    medium = (medium.value * factor).dp,
    large = (large.value * factor).dp,
    xLarge = (xLarge.value * factor).dp,
    xxLarge = (xxLarge.value * factor).dp,
    xxxLarge = (xxxLarge.value * factor).dp
)

/**
 * Scale extension function for ComponentSize
 */
fun AppDimensions.ComponentSize.scale(factor: Float): AppDimensions.ComponentSize = AppDimensions.ComponentSize(
    touchTarget = (touchTarget.value * factor).dp,
    iconTiny = (iconTiny.value * factor).dp,
    iconSmall = (iconSmall.value * factor).dp,
    iconMedium = (iconMedium.value * factor).dp,
    iconLarge = (iconLarge.value * factor).dp,
    iconExtraLarge = (iconExtraLarge.value * factor).dp,
    button = (button.value * factor).dp,
    buttonLarge = (buttonLarge.value * factor).dp,
    appBar = (appBar.value * factor).dp,
    bottomBar = (bottomBar.value * factor).dp,
    listItem = (listItem.value * factor).dp,
    listItemSmall = (listItemSmall.value * factor).dp,
    listItemLarge = (listItemLarge.value * factor).dp,
    inputField = (inputField.value * factor).dp,
    card = (card.value * factor).dp,
    dialog = (dialog.value * factor).dp,
    divider = divider, // Don't scale divider thickness
    contentPadding = (contentPadding.value * factor).dp,
    screenEdgePadding = (screenEdgePadding.value * factor).dp,
    itemSpacing = (itemSpacing.value * factor).dp
)

/**
 * Scale extension function for Radius
 */
fun AppDimensions.Radius.scale(factor: Float): AppDimensions.Radius = AppDimensions.Radius(
    none = none,
    tiny = (tiny.value * factor).dp,
    small = (small.value * factor).dp,
    medium = (medium.value * factor).dp,
    large = (large.value * factor).dp,
    xLarge = (xLarge.value * factor).dp,
    xxLarge = (xxLarge.value * factor).dp,
    circular = circular // Don't scale circular radius
)

/**
 * Scale extension function for Elevation
 */
fun AppDimensions.Elevation.scale(factor: Float): AppDimensions.Elevation = AppDimensions.Elevation(
    none = none,
    tiny = (tiny.value * factor).dp,
    small = (small.value * factor).dp,
    medium = (medium.value * factor).dp,
    large = (large.value * factor).dp,
    xLarge = (xLarge.value * factor).dp
)

// CompositionLocal for dimensions
val LocalAppDimensions = compositionLocalOf {
    AppDimensions.Dimensions()
}

/**
 * Returns appropriate dimensions based on device characteristics including orientation
 */
@Composable
fun rememberAppDimensions(windowSizeClass: WindowSizeClass? = null): AppDimensions.Dimensions {
    // Use LocalDensity for device density information
    val density = LocalDensity.current

    // Get window info for modern window size detection
    val windowInfo = LocalWindowInfo.current

    // Get configuration for orientation
    val configuration = LocalConfiguration.current

    // Determine orientation
    val orientation = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        AppDimensions.Orientation.LANDSCAPE
    } else {
        AppDimensions.Orientation.PORTRAIT
    }

    return remember(density, windowSizeClass, orientation, windowInfo.containerSize.width) {
        // Determine device type based on window size class or container size
        val deviceType = when (windowSizeClass?.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> AppDimensions.DeviceType.LARGE_TABLET
            WindowWidthSizeClass.Medium -> AppDimensions.DeviceType.TABLET
            WindowWidthSizeClass.Compact -> AppDimensions.DeviceType.PHONE
            // Fall back to LocalWindowInfo if windowSizeClass is not available
            else -> {
                // Convert container width from pixels to dp
                val screenWidthDp = with(density) {
                    windowInfo.containerSize.width.toDp().value.toInt()
                }
                when {
                    screenWidthDp >= 900 -> AppDimensions.DeviceType.LARGE_TABLET
                    screenWidthDp >= 600 -> AppDimensions.DeviceType.TABLET
                    else -> AppDimensions.DeviceType.PHONE
                }
            }
        }

        // Check if this is a Pixel device that needs correction
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val isPixelDevice = manufacturer.equals("Google", ignoreCase = true)

        // Log device information with proper formatting
        Timber.tag(TAG).d(
            "Device info - Manufacturer: %s, Model: %s, Device Type: %s, Orientation: %s, Density: %f, Container Width: %d px",
            manufacturer, model, deviceType, orientation, density.density, windowInfo.containerSize.width
        )

        AppDimensions.Dimensions.forDeviceType(
            deviceType = deviceType,
            orientation = orientation,
            pixelCorrection = isPixelDevice
        )
    }
}