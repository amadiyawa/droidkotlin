package com.amadiyawa.feature_profile.presentation.screen.aboutscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amadiyawa.feature_base.presentation.compose.composable.EmptyScreen
import com.amadiyawa.feature_profile.R

@Composable
fun AboutScreen(
    onBackClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    onRateAppClick: () -> Unit = {}
) {
    EmptyScreen(
        title = stringResource(R.string.about),
        message = stringResource(R.string.about_coming_soon)
    )
}

@Preview(showBackground = true)
@Composable
private fun AboutScreenPreview() {
    AboutScreen()
}