package com.amadiyawa.feature_users.presentation.screen.compose.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    isVisible: Boolean,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically {
            with(density) { 40.dp.roundToPx() }
        } + fadeIn(),
        exit = fadeOut(
            animationSpec = keyframes {
                this.durationMillis = 120
            }
        )
    ) {
        FloatingActionButton(
            onClick = onClick,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ){
            Icon(imageVector,"", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}