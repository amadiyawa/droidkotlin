package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
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
    centered: Boolean,
    title: String = stringResource(id = R.string.empty_toolbar_title),
    onBackClick: (() -> Unit) = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
){
    if (centered) {
        CenterAlignedTopAppBar(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimension.gridTwo),
            title = {
                TextTitleLarge(
                    text = title,
                    modifier = Modifier.padding(start = Dimen.Spacing.large),
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = backgroundColor
            ),
            navigationIcon = {
                Row(modifier = Modifier.padding(horizontal = MaterialTheme.dimension.gridTwo)) {
                    CircularButton(
                        circularButtonParams = CircularButtonParams(
                            onClick = onBackClick,
                            enabled = true,
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            description = stringResource(id = R.string.back),
                        ),
                        size = MaterialTheme.dimension.gridFour,
                        color = MaterialTheme.colorScheme.surface,
                        onColor = MaterialTheme.colorScheme.primary
                    )
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )
    } else {
        TopAppBar(
            title = {
                TextTitleLarge(
                    text = title,
                    modifier = Modifier.padding(start = 10.dp),
                )
            },
            actions = {

            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor),
            windowInsets = WindowInsets(0, 0, 0, 0)
        )
    }
}