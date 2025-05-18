package com.amadiyawa.feature_profile.presentation.screen.notificationsettings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amadiyawa.feature_base.presentation.compose.composable.EmptyScreen
import com.amadiyawa.feature_profile.R

@Composable
fun NotificationSettingsScreen(
    onBackClick: () -> Unit = {}
) {
    EmptyScreen(
        title = stringResource(R.string.notification_settings),
        message = stringResource(R.string.notification_settings_coming_soon)
    )
}

@Preview(showBackground = true)
@Composable
private fun NotificationSettingsScreenPreview() {
    NotificationSettingsScreen()
}