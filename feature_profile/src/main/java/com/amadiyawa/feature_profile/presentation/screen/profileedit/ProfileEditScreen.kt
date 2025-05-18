package com.amadiyawa.feature_profile.presentation.screen.profileedit

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.amadiyawa.feature_base.presentation.compose.composable.EmptyScreen
import com.amadiyawa.feature_profile.R

@Composable
fun ProfileEditScreen(
    section: String = "general",
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    EmptyScreen(
        title = stringResource(R.string.edit_profile),
        message = stringResource(R.string.edit_profile_coming_soon)
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditScreenPreview() {
    ProfileEditScreen()
}