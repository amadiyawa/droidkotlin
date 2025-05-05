package com.amadiyawa.feature_auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_base.presentation.compose.composable.ButtonIconType
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButton
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyMedium
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun SocialAuthFooter(
    modifier: Modifier = Modifier,
    onGoogleSignIn: () -> Unit = {},
    onFacebookSignIn: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.dimension.spacing.medium)
            .navigationBarsPadding() // Ensure safe distance from system UI
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = MaterialTheme.dimension.spacing.medium,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            TextBodyMedium(
                text = stringResource(id = R.string.continue_with),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.medium))

        // Social auth buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SocialAuthButton(
                icon = R.drawable.ic_google,
                onClick = onGoogleSignIn,
                contentDescription = "Sign in with Google"
            )

            SocialAuthButton(
                icon = R.drawable.ic_facebook,
                onClick = onFacebookSignIn,
                contentDescription = "Sign in with Facebook"
            )
        }
    }
}

@Composable
private fun SocialAuthButton(
    icon: Int,
    onClick: () -> Unit,
    contentDescription: String
) {
    CircularButton(
        params = CircularButtonParams(
            iconType = ButtonIconType.Resource(icon),
            backgroundColor = MaterialTheme.colorScheme.surface,
            borderWidth = MaterialTheme.dimension.grid.quarter,
            borderColor = MaterialTheme.colorScheme.outline,
            onClick = onClick,
            description = contentDescription
        )
    )
}