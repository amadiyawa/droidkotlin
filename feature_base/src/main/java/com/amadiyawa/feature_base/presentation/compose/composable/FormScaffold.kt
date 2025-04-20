package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun FormScaffold(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    topBar: @Composable (() -> Unit)? = {
        Toolbar(
            centered = true,
            backgroundColor = MaterialTheme.colorScheme.background
        )
    },
    content: @Composable ColumnScope.() -> Unit
) {
    val gridTwo = MaterialTheme.dimension.gridTwo

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = { topBar?.invoke() }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = gridTwo)
        ) {
            FormContentArea(
                modifier = Modifier.weight(1f),
                scrollState = scrollState,
                content = content
            )
        }
    }
}

@Composable
fun FormContentArea(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    val gridThree = MaterialTheme.dimension.gridThree

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(
                space = gridThree,
                alignment = verticalAlignment
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}