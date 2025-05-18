package com.amadiyawa.feature_profile.presentation.screen.privacypolicy

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amadiyawa.feature_base.presentation.compose.composable.EmptyScreen
import com.amadiyawa.feature_profile.R

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit = {}
) {
    EmptyScreen(
        title = stringResource(R.string.privacy_policy),
        message = stringResource(R.string.privacy_policy_coming_soon)
    )
}

@Preview(showBackground = true)
@Composable
private fun PrivacyPolicyScreenPreview() {
    PrivacyPolicyScreen()
}