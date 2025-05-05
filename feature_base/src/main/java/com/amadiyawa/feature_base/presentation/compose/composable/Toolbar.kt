package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.amadiyawa.feature_base.presentation.theme.dimension

data class ToolbarParams(
    val modifier: Modifier = Modifier,
    val showBackButton: Boolean = false,
    val title: String? = null,
    val onBackPressed: (() -> Unit)? = null,
    val navIcon : ButtonIconType = ButtonIconType.Vector(Icons.AutoMirrored.Filled.ArrowBack),
)

@Composable
fun Toolbar(
    params: ToolbarParams,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    actions: @Composable RowScope.() -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(horizontal = MaterialTheme.dimension.spacing.medium)
) {
    if (!params.showBackButton && params.title == null && actions == {}) return

    Surface(
        color = backgroundColor,
        modifier = params.modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimension.componentSize.appBar)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation section
            BackButtonSection(params.navIcon, params.showBackButton, params.onBackPressed)

            // Title section
            TitleSection(params.title, params.showBackButton)

            // Actions section
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
    }
}

@Composable
private fun RowScope.BackButtonSection(
    navIcon: ButtonIconType,
    showBackButton: Boolean,
    onBackPressed: (() -> Unit)?
) {
    if (showBackButton) {
        CircularButton(
            params = CircularButtonParams(
                iconType = navIcon,
                backgroundColor = MaterialTheme.colorScheme.surface,
                borderWidth = MaterialTheme.dimension.grid.quarter,
                iconTint = MaterialTheme.colorScheme.primary,
                onClick = { onBackPressed?.invoke() },
                description = "Navigate back"
            )
        )

        // Add 8dp spacing after back button
        Spacer(modifier = Modifier.width(MaterialTheme.dimension.spacing.small))
    }
}

@Composable
private fun RowScope.TitleSection(
    title: String?,
    showBackButton: Boolean
) {
    if (title != null) {
        val startPadding = if (!showBackButton)
            MaterialTheme.dimension.spacing.none
        else
            MaterialTheme.dimension.spacing.medium

        TextTitleLarge(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = startPadding),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = if (!showBackButton) TextAlign.Start else TextAlign.Center
        )
    } else {
        Spacer(modifier = Modifier.weight(1f))
    }
}