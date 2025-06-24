package com.amadiyawa.feature_base.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.amadiyawa.feature_base.common.util.getCustomColors

typealias Theme = MaterialTheme

val LocalCustomColor = compositionLocalOf<CustomColor> { error("No custom color provided") }
val LocalAccentColor = compositionLocalOf<AccentColor> { error("No accent color provided") }

val Theme.customColor: CustomColor
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColor.current

val Theme.accentColor: AccentColor
    @Composable
    @ReadOnlyComposable
    get() = LocalAccentColor.current

val Theme.dimension: AppDimensions.Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalAppDimensions.current

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    secondary = DarkSecondary,
    secondaryContainer = DarkSecondaryContainer,
    tertiary = DarkTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHighest = DarkSurfaceContainerHigh,
    surfaceContainerLowest = DarkSurfaceContainerLow,
    error = DarkError,
    outline = DarkOutline,
    onPrimary = DarkOnPrimary,
    onPrimaryContainer = DarkOnPrimaryContainer,
    onSecondary = DarkOnSecondary,
    onSecondaryContainer = DarkOnSecondaryContainer,
    onTertiary = DarkOnTertiary,
    onTertiaryContainer = DarkOnTertiaryContainer,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    onError = DarkOnError
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    primaryContainer = LightPrimaryVariant,
    secondary = LightSecondary,
    secondaryContainer = LightSecondaryVariant,
    tertiary = LightTertiary,
    tertiaryContainer = LightTertiaryContainer,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHighest = LightSurfaceContainerHigh,
    surfaceContainerLowest = LightSurfaceContainerLow,
    error = LightError,
    outline = LightOutline,
    onPrimary = LightOnPrimary,
    onPrimaryContainer = LightOnPrimaryContainer,
    onSecondary = LightOnSecondary,
    onSecondaryContainer = LightOnSecondaryContainer,
    onTertiary = LightOnTertiary,
    onTertiaryContainer = LightOnTertiaryContainer,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    onError = LightOnError
)

/**
 * Composable function to set the application's theme.
 *
 * @param darkTheme A boolean indicating whether the dark theme is enabled. Defaults to the system's dark theme setting.
 * @param dynamicColor A boolean indicating whether to use dynamic colors. Available on Android 12+.
 * @param content The composable content to be displayed within the theme.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    windowSizeClass: WindowSizeClass? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Get dimensions with proper device correction
    val dimensions = rememberAppDimensions(windowSizeClass)

    CompositionLocalProvider(
        LocalCustomColor provides getCustomColors(darkTheme),
        LocalAccentColor provides AccentColor(),
        LocalAppDimensions provides dimensions,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}