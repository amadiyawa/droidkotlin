package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.theme.dimension
import com.amadiyawa.paygo.base.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    hasNavigationIcon: Boolean,
    title: String = stringResource(id = R.string.empty_toolbar_title),
    onBackClick: (() -> Unit) = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
){
    TopAppBar(
        modifier = modifier.padding(horizontal = MaterialTheme.dimension.spacing.medium),
        navigationIcon = if (hasNavigationIcon) {
            { NavigationIcon(onBackClick) }
        } else { {} },
        title = {
            TextTitleLarge(
                text = title,
                modifier = if (hasNavigationIcon) Modifier.padding(start = Dimen.Spacing.large) else Modifier.padding(start = 10.dp),
            )
        },
        actions = {

        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = backgroundColor
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

@Composable
private fun NavigationIcon(onBackClick: () -> Unit) {
    Row {
        CircularButton(
            params = CircularButtonParams(
                iconType = ButtonIconType.Vector(Icons.AutoMirrored.Filled.ArrowBack),
                backgroundColor = MaterialTheme.colorScheme.surface,
                borderWidth = MaterialTheme.dimension.grid.quarter,
                iconTint = MaterialTheme.colorScheme.primary,
                onClick = { onBackClick },
                description = "Back click"
            )
        )
    }
}