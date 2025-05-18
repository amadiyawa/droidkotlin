package com.amadiyawa.feature_auth.presentation.screen.authcheck

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amadiyawa.feature_base.domain.manager.UserSessionManager
import com.amadiyawa.feature_base.domain.model.SessionState
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import org.koin.compose.koinInject

@Composable
fun AuthCheckScreen(
    onAuthenticated: () -> Unit,
    onNotAuthenticated: () -> Unit
) {
    val userSessionManager: UserSessionManager = koinInject()
    val sessionState by userSessionManager.sessionState.collectAsState(
        initial = SessionState.Loading
    )

    LaunchedEffect(sessionState) {
        when (sessionState) {
            is SessionState.Authenticated -> {
                // User is authenticated, navigate to main
                onAuthenticated()
            }
            is SessionState.NotAuthenticated -> {
                // User is not authenticated, show welcome
                onNotAuthenticated()
            }
            is SessionState.Loading -> {
                // Still loading initial session, wait
            }
            is SessionState.Authenticating -> {
                // Authentication in progress, wait
            }
        }
    }

    // Show loading for all waiting states
    val isLoading = sessionState is SessionState.Loading || sessionState is SessionState.Authenticating

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation(visible = isLoading)
    }
}