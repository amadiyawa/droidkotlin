package com.amadiyawa.feature_base.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

interface NavDestinationContract {
    val route: String
    val destination: String
    val title: Int
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val placement: DestinationPlacement
}