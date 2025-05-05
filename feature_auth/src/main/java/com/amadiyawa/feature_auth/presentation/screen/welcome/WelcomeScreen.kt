package com.amadiyawa.feature_auth.presentation.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.presentation.components.SocialAuthFooter
import com.amadiyawa.feature_base.presentation.compose.composable.AppOutlinedButton
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
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
        Spacer(modifier = Modifier.weight(1f))

        SocialAuthFooter()
    }
}

@Composable
internal fun SignInSignUpButtons(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
) {
    Column {
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
}