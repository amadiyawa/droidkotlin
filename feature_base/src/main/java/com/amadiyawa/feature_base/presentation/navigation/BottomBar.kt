package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.amadiyawa.feature_base.presentation.compose.composable.TextLabelSmall
import com.amadiyawa.feature_base.presentation.compose.composable.barItemColors
import com.amadiyawa.feature_base.presentation.theme.dimension
import timber.log.Timber
import kotlin.collections.sortedBy

/**
 * Composable function that creates a custom bottom navigation bar.
 *
 * This function displays a navigation bar with items based on the provided destinations.
 * It highlights the currently selected destination and triggers navigation actions
 * when an item is clicked.
 *
 * @param modifier The [Modifier] to be applied to the navigation bar.
 * @param destinations A list of [NavDestinationContract] representing the navigation items.
 * @param onNavigate A lambda function invoked when a navigation item is clicked, passing the selected [NavDestinationContract].
 * @param currentDestination The current [NavDestination] being displayed, used to determine the selected item.
 */
@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    destinations: List<NavigationDestination>,
    onNavigate: (String) -> Unit,
    currentDestination: NavDestination?
) {
    // 1. Sort destinations by order property
    val sortedDestinations = remember(destinations) {
        destinations.sortedBy { it.order }
    }

    // Debug current routes
    val currentRoute = currentDestination?.route
    Timber.d("CustomBottomBar: Current destination route: $currentRoute")
    Timber.d("CustomBottomBar: Destinations to show: ${sortedDestinations.map { it.route }}")

    NavigationBar(
        modifier = modifier
            .navigationBarsPadding()
            .height(MaterialTheme.dimension.componentSize.bottomBar),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        sortedDestinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                Timber.d("Route comparison: ${it.route} == ${destination.route} = ${it.route == destination.route}")
                it.route == destination.route
            } == true

            val title = stringResource(id = destination.title)

            // Debug each item
            Timber.d("Bottom bar item: ${destination.route}, selected=$selected, currentRoute=$currentRoute")

            NavigationBarItem(
                modifier = Modifier.semantics {
                    contentDescription = "Navigate to ${destination.title}"
                    role = Role.Tab
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        Timber.d("Navigating to: ${destination.route}")
                        onNavigate(destination.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = title
                    )
                },
                label = {
                    TextLabelSmall(
                        modifier = Modifier.padding(top = MaterialTheme.dimension.spacing.xSmall),
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (selected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                alwaysShowLabel = true,
                colors = barItemColors()
            )
        }
    }
}