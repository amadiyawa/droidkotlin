package com.amadiyawa.feature_base.presentation.theme

import androidx.compose.ui.graphics.Color

internal const val WHITE = "#FFFFFF"
internal const val BLACK = "#000000"

// Material color
val LightPrimary = Color(0xFF1E88E5) // New Blue
val LightPrimaryVariant = Color(0xFF1565C0) // Darker Blue
val LightSecondary = Color(0xFF29B6F6) // Gold
val LightSecondaryVariant = Color(0xFF0288D1) // GoldenRod
val LightTertiary = Color(0xFF80CBC4) // Medium Blue, for tertiary elements
val LightTertiaryContainer = Color(0xFFB2DFDB) // Light Blue, for tertiary containers
val LightSurface = Color(0xFFF5F5F5) // Light Grey
val LightSurfaceVariant = Color(0xFFE0E0E0) // Light Grey, for an alternate surface
val LightBackground = Color(0xFFFFFFFF) // White
val LightError = Color(0xFFB00020) // Dark Red
val LightOnPrimary = Color(0xFFFFFFFF) // White
val LightOnPrimaryContainer = Color(0xFF1E88E5) // New Blue
val LightOnSecondary = Color(0xFF000000) // Black
val LightOnSecondaryContainer = Color(0xFF0288D1) // GoldenRod
val LightOnTertiary = Color(0xFF004D40) // Medium Blue, for tertiary elements
val LightOnTertiaryContainer = Color(0xFF004D40) // Medium Blue, for text on tertiary container
val LightOnSurface = Color(0xFF000000) // Dark Grey
val LightOnSurfaceVariant = Color(0xFF757575) // Grey, for text or elements on alternate surface
val LightOnBackground = Color(0xFF000000) // Black
val LightOnError = Color(0xFFFFFFFF) // White
val LightOutline = Color(0xFFBDBDBD) // Grey, for outlines

val DarkPrimary = Color(0xFF1E88E5) // New Blue
val DarkPrimaryContainer = Color(0xFF1565C0) // Darker Blue
val DarkSecondary = Color(0xFF29B6F6) // Gold
val DarkSecondaryContainer = Color(0xFF0288D1) // GoldenRod
val DarkTertiary = Color(0xFF004D40) // Medium Blue, for tertiary elements
val DarkTertiaryContainer = Color(0xFF00796B) // Dark Blue, for tertiary containers
val DarkSurface = Color(0xFF1E1E1E) // Very Dark Grey
val DarkSurfaceVariant = Color(0xFF2C2C2C) // Dark Grey, for an alternate surface
val DarkBackground = Color(0xFF121212) // Black
val DarkError = Color(0xFFCF6679) // Dark Pink
val DarkOnPrimary = Color(0xFFFFFFFF) // White
val DarkOnPrimaryContainer = Color(0xFFBBDEFB) // Light Blue
val DarkOnSecondary = Color(0xFFFFFFFF) // Black
val DarkOnSecondaryContainer = Color(0xFFB3E5FC) // Light Blue
val DarkOnTertiary = Color(0xFFB2DFDB) // Black, for text or elements on tertiary background
val DarkOnTertiaryContainer = Color(0xFFB2DFDB) // Light Blue, for text on tertiary container
val DarkOnSurface = Color(0xFFf0eef1) // Very Light Grey
val DarkOnSurfaceVariant = Color(0xFFBDBDBD) // Grey, for text or elements on alternate surface
val DarkOnBackground = Color(0xFFFFFFFF) // White
val DarkOnError = Color(0xFF000000) // Black
val DarkOutline = Color(0xFF757575) // Grey, for outlines

// Custom color
val lightSuccess = Color(0xFF4CAF50)
val lightOnSuccess = Color(0xFFFFFFFF)
val lightWarning = Color(0xFFFFC107)
val lightOnWarning = Color(0xFF000000)
val lightInfo = Color(0xFF2196F3)
val lightOnInfo = Color(0xFFFFFFFF)
val lightDanger = Color(0xFFF44336)
val lightOnDanger = Color(0xFFFFFFFF)

val darkSuccess = Color(0xFF81C784)
val darkOnSuccess = Color(0xFF000000)
val darkWarning = Color(0xFFFFD54F)
val darkOnWarning = Color(0xFF000000)
val darkInfo = Color(0xFF64B5F6)
val darkOnInfo = Color(0xFF000000)
val darkDanger = Color(0xFFE57373)
val darkOnDanger = Color(0xFF000000)

// Accent colors
val primaryAccent = Color(0xFF2196F3) // DodgerBlue, buttons, links, or highlights.
val warningAccent = Color(0xFFFF5722) // Coral, warning or error messages.
val neutralGray = Color(0xFF808080) // DimGray, borders, dividers, or disabled elements.
val darkAccentBackground = Color(0xFF2F3135) // Charcoal, background of dialogs, cards, or sheets.
val successAccent = Color(0xFF98FB98)  // MintCream, success messages.
val errorAccent = Color(0xFFFF6961)  // Tomato, error messages.
val lightAccent = Color(0xFF87CEFA) // SkyBlue, background of selected items or elements.
val brightAccent = Color(0xFF00BFFF) // DeepSkyBlue, background of active elements or elements in focus.

/**
 * Specifies the custom light's colours that should be used through the application in a non-graphic
 * library specific amount.
 */
data object CustomLightColorDefaults {
    internal const val LIGHTSUCESS = "#4CAF50"
    internal const val LIGHTWARNING = "#FFC107"
    internal const val LIGHTINFO = "#2196F3"
    internal const val LIGHTDANGER = "#F44336"
    internal const val LIGHTONSUCESS = WHITE
    internal const val LIGHTONWARNING = "#000000"
    internal const val LIGHTONINFO = WHITE
    internal const val LIGHTONDANGER = WHITE
}

/**
 * Specifies the custom dark's colours that should be used through the application in a non-graphic
 * library specific amount.
 */
data object CustomDarkColorDefaults {
    internal const val DARKSUCESS = "#81C784"
    internal const val DARKWARNING = "#FFD54F"
    internal const val DARKINFO = "#64B5F6"
    internal const val DARKDANGER = "#E57373"
    internal const val DARKONSUCESS = BLACK
    internal const val DARKONWARNING = BLACK
    internal const val DARKONINFO = BLACK
    internal const val DARKONDANGER = BLACK
}

/**
 * Specifies the accent's colours that should be used through the application in a non-graphic
 * library specific amount.
 */
data object AccentColorDefaults {
    internal const val PRIMARY = "#2196F3"
    internal const val WARNING = "#FFC107"
    internal const val NEUTRAL = "#808080"
    internal const val DARKBACKGROUND = "#2F3135"
    internal const val SUCCESS = "#98FB98"
    internal const val ERROR = "#FF6961"
    internal const val LIGHT = "#87CEFA"
    internal const val BRIGHT = "#00BFFF"
}

data class CustomColor(
    val success: Color,
    val warning: Color,
    val info: Color,
    val danger: Color,
    val onSuccess: Color,
    val onWarning: Color,
    val onInfo: Color,
    val onDanger: Color
)

data class AccentColor(
    val primary: Color,
    val warning: Color,
    val neutral: Color,
    val darkBackground: Color,
    val success: Color,
    val error: Color,
    val light: Color,
    val bright: Color
)