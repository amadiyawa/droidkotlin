package com.amadiyawa.feature_auth.presentation.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_base.presentation.compose.composable.AppOutlinedButton
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.ButtonIconType
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButton
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyMedium
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun WelcomeScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimension.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section: Logo, title and description
        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.xxLarge))

        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(MaterialTheme.dimension.componentSize.card)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.large))

        AuthHeader(
            title = stringResource(id = R.string.auth_welcome_title),
            description = stringResource(id = R.string.auth_welcome_description),
            centerContent = true
        )

        // Center section: Sign in and Sign up buttons
        Spacer(modifier = Modifier.weight(1f))

        SignInSignUpButtons(
            onSignIn = onSignIn,
            onSignUp = onSignUp
        )

        // Bottom section: Social login options
        Spacer(modifier = Modifier.weight(0.5f))

        AuthFooter()
    }
}

@Composable
internal fun SignInSignUpButtons(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
) {
    FilledButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimension.componentSize.buttonLarge),
        text = stringResource(id = R.string.login),
        onClick = onSignIn
    )

    Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.medium))

    AppOutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimension.componentSize.buttonLarge),
        text = stringResource(id = R.string.register),
        onClick = onSignUp
    )
}

@Composable
fun AuthFooter() {
    Column {
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
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )

            TextBodyMedium(
                text = stringResource(id = R.string.continue_with),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CircularButton(
                params = CircularButtonParams(
                    iconType = ButtonIconType.Resource(R.drawable.ic_google),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderWidth = MaterialTheme.dimension.grid.quarter,
                    borderColor = MaterialTheme.colorScheme.outline,
                    onClick = { /* Handle Google sign-in */ },
                    description = "SignIn using Google"
                )
            )

            CircularButton(
                params = CircularButtonParams(
                    iconType = ButtonIconType.Resource(R.drawable.ic_facebook),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderWidth = MaterialTheme.dimension.grid.quarter,
                    borderColor = MaterialTheme.colorScheme.outline,
                    onClick = { /* Handle Facebook sign-in */ },
                    description = "SignIn using Facebook"
                )
            )
        }
    }
}