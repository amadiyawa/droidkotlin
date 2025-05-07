package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.amadiyawa.droidkotlin.base.R

@Composable
fun ExpandableRow(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onRowClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRowClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier
        ) {
            Text(
                text = if (expanded) stringResource(id = R.string.show_less) else stringResource(id = R.string.show_more),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandCircleDown,
                contentDescription = "left arrow",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}