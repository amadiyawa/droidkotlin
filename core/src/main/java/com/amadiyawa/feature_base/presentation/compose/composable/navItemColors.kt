package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable

/**
 * Creates and returns the default color configuration for a NavigationBarItem.
 *
 * This function defines the colors used for the icons, text, and indicator
 * in both selected and unselected states, based on the application's MaterialTheme.
 *
 * @return A [NavigationBarItemColors] instance with the specified color configuration.
 */
@Composable
fun barItemColors(): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
    )
}

/**
 * Creates and returns the default color configuration for a NavigationRailItem.
 *
 * This function defines the colors used for the icons, text, and indicator
 * in both selected and unselected states, based on the application's MaterialTheme.
 *
 * @return A [NavigationRailItemColors] instance with the specified color configuration.
 */
@Composable
fun railItemColors(): NavigationRailItemColors {
    return NavigationRailItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
    )
}
