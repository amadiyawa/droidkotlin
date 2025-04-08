package com.amadiyawa.feature_users.presentation.screen.compose.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_user.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    centered: Boolean,
    title: String = stringResource(id = R.string.empty_toolbar_title),
    onBackClick: (() -> Unit)?,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
){
    if (centered) {
        CenterAlignedTopAppBar(
            title = {
                TextTitleLarge(
                    text = title,
                    modifier = Modifier.padding(start = 10.dp),
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = backgroundColor
            ),
            navigationIcon = {
                IconButton(onClick = onBackClick!!) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = MaterialTheme.colorScheme.onSurface
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
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor
            ),
            windowInsets = WindowInsets(0, 0, 0, 0)
        )
    }
}