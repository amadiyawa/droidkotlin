package com.amadiyawa.feature_profile.presentation.screen.profilesettings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amadiyawa.feature_base.presentation.compose.composable.EmptyScreen
import com.amadiyawa.feature_profile.R

@Composable
fun ProfileSettingsScreen(
    onBackClick: () -> Unit = {},
    onThemeChanged: () -> Unit = {},
    onLanguageChanged: () -> Unit = {},
    onNotificationSettingsClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsOfServiceClick: () -> Unit = {}
) {
    EmptyScreen(
        title = stringResource(R.string.settings),
        message = stringResource(R.string.settings_coming_soon)
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileSettingsScreenPreview() {
    ProfileSettingsScreen()
}