package com.amadiyawa.feature_auth.presentation.screen.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.compose.composable.AppOutlinedButton
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.SocialButton
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyMedium
import com.amadiyawa.feature_base.presentation.compose.composable.TextHeadlineMedium
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun WelcomeScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {
    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {  }
    ) { paddingValues ->
        SetupContent(
            paddingValues = paddingValues,
            onSignIn = onSignIn,
            onSignUp = onSignUp
        )
    }
}

@Composable
private fun SetupContent(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.dimension.gridTwo)
    ) {
        AuthHeader()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimension.gridThree,
                Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignInSignUpButtons(
                onSignIn = onSignIn,
                onSignUp = onSignUp
            )
        }

        AuthFooter()
    }
}

@Composable
private fun AuthHeader() {
    Column(
        modifier = Modifier
            .padding(top = MaterialTheme.dimension.gridFour)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimension.gridTwo,
            Alignment.CenterVertically
        )
    ) {
        Icon(
            modifier = Modifier.size(Dimen.Image.medium),
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = null,
            tint = Color.Unspecified
        )
        TextHeadlineMedium(text = stringResource(id = R.string.auth_welcome))
        TextBodyLarge(
            text = stringResource(id = R.string.auth_welcome_description),
            textAlign = TextAlign.Center
        )
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
            .requiredHeight(Dimen.Size.extraLarge),
        text = stringResource(id = R.string.login),
        onClick = onSignIn
    )
    AppOutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(Dimen.Size.extraLarge),
        text = stringResource(id = R.string.register),
        onClick = onSignUp
    )
}

@Composable
fun AuthFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.dimension.gridFour),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimension.gridThree,
            Alignment.CenterVertically
        )
    ) {
        TextBodyMedium(text = stringResource(id = R.string.continue_with))

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimension.gridTwo,
                Alignment.CenterHorizontally
            )
        ) {
            SocialButton(
                onClick = {  },
                painter = painterResource(id = R.drawable.ic_google),
                size = MaterialTheme.dimension.gridSix
            )

            SocialButton(
                onClick = {  },
                painter = painterResource(id = R.drawable.ic_facebook),
                size = MaterialTheme.dimension.gridSix
            )
        }
    }
}