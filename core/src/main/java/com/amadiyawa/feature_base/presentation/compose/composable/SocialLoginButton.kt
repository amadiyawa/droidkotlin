package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun SocialLoginButton(
    icon: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(MaterialTheme.dimension.componentSize.buttonLarge)
            .clip(CircleShape)
            .border(
                width = MaterialTheme.dimension.grid.quarter,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Social Login",
            modifier = Modifier.size(MaterialTheme.dimension.componentSize.iconMedium),
            tint = Color.Unspecified
        )
    }
}