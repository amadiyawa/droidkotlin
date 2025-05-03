package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun FormScaffold(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    config: FormConfig = FormConfig(),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
    ) {
        // Custom Top Bar
        FormHeader(
            showBackButton = config.showBackButton,
            title = config.title,
            onBackPressed = config.onBackPressed
        )

        // Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(config.contentPadding),
                verticalArrangement = Arrangement.spacedBy(
                    space = MaterialTheme.dimension.spacing.medium
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )

            // Handle scroll shadows
            ScrollShadows(
                scrollState = scrollState,
                alignment = Alignment.TopCenter
            )

            ScrollShadows(
                scrollState = scrollState,
                alignment = Alignment.BottomCenter
            )
        }
    }
}

@Composable
private fun FormHeader(
    showBackButton: Boolean,
    title: String?,
    onBackPressed: (() -> Unit)?
) {
    if (!showBackButton && title == null) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimension.componentSize.appBar)
            // Standard horizontal padding of 16dp
            .padding(horizontal = MaterialTheme.dimension.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButtonSection(showBackButton, onBackPressed)
        TitleSection(title, showBackButton)

        // Add standard 16dp spacing between header and content
        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.medium))
    }
}

@Composable
private fun RowScope.BackButtonSection(
    showBackButton: Boolean,
    onBackPressed: (() -> Unit)?
) {
    if (showBackButton) {
        CircularButton(
            params = CircularButtonParams(
                iconType = ButtonIconType.Vector(Icons.AutoMirrored.Filled.ArrowBack),
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
        val startPadding = if (showBackButton)
            MaterialTheme.dimension.spacing.none
        else
            MaterialTheme.dimension.spacing.medium

        TextTitleLarge(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = startPadding),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = if (showBackButton) TextAlign.Start else TextAlign.Center
        )
    } else {
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ScrollShadows(
    scrollState: ScrollState,
    alignment: Alignment
) {
    val isTop = alignment == Alignment.TopCenter
    val canShow = if (isTop) scrollState.canScrollBackward else scrollState.canScrollForward

    if (canShow) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimension.spacing.small)
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isTop) {
                            listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                                Color.Transparent
                            )
                        } else {
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                            )
                        }
                    )
                )
        )
    }
}

data class FormConfig(
    val showBackButton: Boolean = true,
    val title: String? = null,
    val onBackPressed: (() -> Unit)? = null,
    val contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
)