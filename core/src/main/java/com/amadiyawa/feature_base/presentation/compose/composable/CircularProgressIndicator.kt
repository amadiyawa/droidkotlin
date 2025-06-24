package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun DefaultCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(MaterialTheme.dimension.componentSize.iconLarge)
            .padding(end = MaterialTheme.dimension.spacing.small),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = MaterialTheme.dimension.radius.tiny,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}