package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun AuthHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    centerContent: Boolean = false
) {
    Column(
        modifier = modifier
            .padding(top = MaterialTheme.dimension.gridFour)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.gridTwo)
    ) {
        TextHeadlineSmall(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = if (centerContent) TextAlign.Center else TextAlign.Start
        )
        TextBodyMedium(
            text = description,
            modifier = Modifier.fillMaxWidth(),
            textAlign = if (centerContent) TextAlign.Center else TextAlign.Start
        )
    }
}