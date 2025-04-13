package com.amadiyawa.feature_auth.presentation.screen.otpverification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium

@Composable
fun OtpVerificationScreen(
    recipient: String,
    onOtpValidated: () -> Unit,
    onCancel: () -> Unit,
) {
    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {}
    ) { paddingValues ->
        SetupContent(paddingValues = paddingValues)
    }
}

@Composable
private fun SetupContent(paddingValues: PaddingValues) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ){
        TextTitleMedium(text = "Welcome to the SignIn")
    }
}