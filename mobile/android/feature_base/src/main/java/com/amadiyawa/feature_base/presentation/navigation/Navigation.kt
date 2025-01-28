package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Data class to manage parameters for navigation items
data class NavigationBarItemParams(
    val selected: Boolean,
    val onClick: () -> Unit,
    val icon: @Composable () -> Unit,
    val selectedIcon: @Composable () -> Unit = icon,
    val enabled: Boolean = true,
    val label: @Composable () -> Unit = { },
    val alwaysShowLabel: Boolean = true,
    val modifier: Modifier = Modifier
)

// Reusable function for navigation bar items
@Composable
fun RowScope.AppNavigationBarItem(params: NavigationBarItemParams) {
    NavigationBarItem(
        selected = params.selected,
        onClick = params.onClick,
        icon = if (params.selected) params.selectedIcon else params.icon,
        modifier = params.modifier,
        enabled = params.enabled,
        label = params.label,
        alwaysShowLabel = params.alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AppNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AppNavigationDefaults.navigationContentColor(),
            selectedTextColor = AppNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AppNavigationDefaults.navigationContentColor(),
            indicatorColor = AppNavigationDefaults.navigationIndicatorColor()
        )
    )
}

// Reusable function for the navigation bar
@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = AppNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content
    )
}

// Reusable function for navigation rail items
@Composable
fun AppNavigationRailItem(params: NavigationBarItemParams) {
    NavigationRailItem(
        selected = params.selected,
        onClick = params.onClick,
        icon = if (params.selected) params.selectedIcon else params.icon,
        modifier = params.modifier,
        enabled = params.enabled,
        label = params.label,
        alwaysShowLabel = params.alwaysShowLabel,
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = AppNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AppNavigationDefaults.navigationContentColor(),
            selectedTextColor = AppNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AppNavigationDefaults.navigationContentColor(),
            indicatorColor = AppNavigationDefaults.navigationIndicatorColor()
        )
    )
}

// Reusable function for the navigation rail
@Composable
fun AppNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        header = header,
        content = content
    )
}

// Object to define default colors for navigation components
object AppNavigationDefaults {
    val navigationBackgroundColor: @Composable () -> Color = { MaterialTheme.colorScheme.surface }
    val navigationContentColor: @Composable () -> Color = { MaterialTheme.colorScheme.onSurface }
    val navigationSelectedItemColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary }
    val navigationIndicatorColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary }
}