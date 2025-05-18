package com.amadiyawa.feature_profile.presentation.screen.termsofservice

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amadiyawa.feature_base.presentation.compose.composable.EmptyScreen
import com.amadiyawa.feature_profile.R

@Composable
fun TermsOfServiceScreen(
    onBackClick: () -> Unit = {}
) {
    EmptyScreen(
        title = stringResource(R.string.terms_of_service),
        message = stringResource(R.string.terms_of_service_coming_soon)
    )
}

@Preview(showBackground = true)
@Composable
private fun TermsOfServiceScreenPreview() {
    TermsOfServiceScreen()
}