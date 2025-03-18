package com.amadiyawa.feature_base.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.presentation.compose.composable.getScreenSizeInfo

/**
 * Object containing default dimension values used throughout the application.
 */
data object DimensionDefaults {
    internal const val GRID_QUARTER = 1.5f
    internal const val GRID_HALF = 3
    internal const val GRID_ONE = 6
    internal const val GRID_ONE_AND_HALF = 9
    internal const val GRID_TWO = 12
    internal const val GRID_TWO_AND_HALF = 15
    internal const val GRID_THREE = 18
    internal const val GRID_THREE_AND_HALF = 21
    internal const val GRID_FOUR = 24
    internal const val GRID_FOUR_AND_HALF = 27
    internal const val GRID_FIVE = 30
    internal const val GRID_FIVE_AND_HALF = 33
    internal const val GRID_SIX = 36
    internal const val MIN_TOUCH_TARGET = 48
    internal const val MIN_LIST_ITEM_HEIGHT = 52
    internal const val APP_BAR_HEIGHT = 32
    internal const val DEFAULT_FULL_PADDING = 16
    internal const val DEFAULT_HALF_PADDING = 8
    internal const val IMAGE_BUTTON_SIZE = 48
    internal const val NAVIGATION_ICON_SIZE = 24
    internal const val WINDOW_WIDTH_COMPACT_HALF = 299
    internal const val WINDOW_WIDTH_COMPACT_ONE_THIRD = 199
    internal const val WINDOW_WIDTH_COMPACT = 599
    internal const val WINDOW_WIDTH_MEDIUM = 839
    internal const val WINDOW_HEIGHT_COMPACT = 379
    internal const val WINDOW_HEIGHT_MEDIUM = 900
}


/**
 * Data class representing dimension values used in the application.
 *
 * @property gridQuarter The dimension value for a quarter grid.
 * @property gridHalf The dimension value for half a grid.
 * @property gridOne The dimension value for one grid.
 * @property gridOneAndHalf The dimension value for one and a half grids.
 * @property gridTwo The dimension value for two grids.
 * @property gridTwoAndHalf The dimension value for two and a half grids.
 * @property gridThree The dimension value for three grids.
 * @property gridThreeAndHalf The dimension value for three and a half grids.
 * @property gridFour The dimension value for four grids.
 * @property gridFourAndHalf The dimension value for four and a half grids.
 * @property gridFive The dimension value for five grids.
 * @property gridFiveAndHalf The dimension value for five and a half grids.
 * @property gridSix The dimension value for six grids.
 * @property minTouchTarget The minimum touch target dimension.
 * @property minListItemHeight The minimum list item height dimension.
 * @property appBarHeight The app bar height dimension.
 * @property defaultFullPadding The default full padding dimension.
 * @property defaultHalfPadding The default half padding dimension.
 * @property imageButtonSize The image button size dimension.
 * @property navigationIconSize The navigation icon size dimension.
 * @property windowWidthCompactHalf The window width for compact half dimension.
 * @property windowWidthCompactOneThird The window width for compact one third dimension.
 * @property windowWidthCompact The window width for compact dimension.
 * @property windowWidthMedium The window width for medium dimension.
 * @property windowHeightCompact The window height for compact dimension.
 * @property windowHeightMedium The window height for medium dimension.
 *
 * @author Amadou Iyawa
 */
data class Dimension(
    val gridQuarter: Dp = DimensionDefaults.GRID_QUARTER.dp,
    val gridHalf: Dp = DimensionDefaults.GRID_HALF.dp,
    val gridOne: Dp = DimensionDefaults.GRID_ONE.dp,
    val gridOneAndHalf: Dp = DimensionDefaults.GRID_ONE_AND_HALF.dp,
    val gridTwo: Dp = DimensionDefaults.GRID_TWO.dp,
    val gridTwoAndHalf: Dp = DimensionDefaults.GRID_TWO_AND_HALF.dp,
    val gridThree: Dp = DimensionDefaults.GRID_THREE.dp,
    val gridThreeAndHalf: Dp = DimensionDefaults.GRID_THREE_AND_HALF.dp,
    val gridFour: Dp = DimensionDefaults.GRID_FOUR.dp,
    val gridFourAndHalf: Dp = DimensionDefaults.GRID_FOUR_AND_HALF.dp,
    val gridFive: Dp = DimensionDefaults.GRID_FIVE.dp,
    val gridFiveAndHalf: Dp = DimensionDefaults.GRID_FIVE_AND_HALF.dp,
    val gridSix: Dp = DimensionDefaults.GRID_SIX.dp,
    val minTouchTarget: Dp = DimensionDefaults.MIN_TOUCH_TARGET.dp,
    val minListItemHeight: Dp = DimensionDefaults.MIN_LIST_ITEM_HEIGHT.dp,
    val appBarHeight: Dp = DimensionDefaults.APP_BAR_HEIGHT.dp,
    val defaultFullPadding: Dp = DimensionDefaults.DEFAULT_FULL_PADDING.dp,
    val defaultHalfPadding: Dp = DimensionDefaults.DEFAULT_HALF_PADDING.dp,
    val imageButtonSize: Dp = DimensionDefaults.IMAGE_BUTTON_SIZE.dp,
    val navigationIconSize: Dp = DimensionDefaults.NAVIGATION_ICON_SIZE.dp,
    val windowWidthCompactHalf: Dp = DimensionDefaults.WINDOW_WIDTH_COMPACT_HALF.dp,
    val windowWidthCompactOneThird: Dp = DimensionDefaults.WINDOW_WIDTH_COMPACT_ONE_THIRD.dp,
    val windowWidthCompact: Dp = DimensionDefaults.WINDOW_WIDTH_COMPACT.dp,
    val windowWidthMedium: Dp = DimensionDefaults.WINDOW_WIDTH_MEDIUM.dp,
    val windowHeightCompact: Dp = DimensionDefaults.WINDOW_HEIGHT_COMPACT.dp,
    val windowHeightMedium: Dp = DimensionDefaults.WINDOW_HEIGHT_MEDIUM.dp,
)

/**
 * Instance of Dimension with default values for small screens.
 */
val smallDimension = Dimension(
    gridQuarter = DimensionDefaults.GRID_QUARTER.dp,
    gridHalf = DimensionDefaults.GRID_HALF.dp,
    gridOne = DimensionDefaults.GRID_ONE.dp,
    gridOneAndHalf = DimensionDefaults.GRID_ONE_AND_HALF.dp,
    gridTwo = DimensionDefaults.GRID_TWO.dp,
    gridTwoAndHalf = DimensionDefaults.GRID_TWO_AND_HALF.dp,
    gridThree = DimensionDefaults.GRID_THREE.dp,
    gridThreeAndHalf = DimensionDefaults.GRID_THREE_AND_HALF.dp,
    gridFour = DimensionDefaults.GRID_FOUR.dp,
    gridFourAndHalf = DimensionDefaults.GRID_FOUR_AND_HALF.dp,
    gridFive = DimensionDefaults.GRID_FIVE.dp,
    gridFiveAndHalf = DimensionDefaults.GRID_FIVE_AND_HALF.dp,
    gridSix = DimensionDefaults.GRID_SIX.dp,
)

/**
 * Instance of Dimension with default values for screens with a width of 360dp.
 */
val sw360Dimension = Dimension(
    gridQuarter = 2.dp,
    gridHalf = 4.dp,
    gridOne = 8.dp,
    gridOneAndHalf = 12.dp,
    gridTwo = 16.dp,
    gridTwoAndHalf = 20.dp,
    gridThree = 24.dp,
    gridThreeAndHalf = 28.dp,
    gridFour = 32.dp,
    gridFourAndHalf = 36.dp,
    gridFive = 40.dp,
    gridFiveAndHalf = 44.dp,
    gridSix = 48.dp,
)

/**
 * Composable function to get the appropriate Dimension instance based on screen size.
 *
 * @return Dimension The appropriate Dimension instance.
 *
 * @author Amadou Iyawa
 */
@Composable
fun getDimension(): Dimension {
    val screenSizeInfo = getScreenSizeInfo()
    return if (screenSizeInfo.widthDp <= 360.dp) smallDimension else sw360Dimension
}

// Create a CompositionLocal to provide the Dimension instance
val LocalDimension = compositionLocalOf<Dimension> { error("No Dimension provided") }