package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
        modifier = modifier.fillMaxSize()
    ) {
        // Custom Top Bar
        Toolbar(
            params = ToolbarParams(
                showBackButton = config.showBackButton,
                title = config.title,
                onBackPressed = config.onBackPressed,
            ),
            backgroundColor = Color.Transparent
        )

        // Add standard 16dp spacing between header and content
        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.medium))

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