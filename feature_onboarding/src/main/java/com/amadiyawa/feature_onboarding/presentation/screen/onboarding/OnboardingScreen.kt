package com.amadiyawa.feature_onboarding.presentation.screen.onboarding

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amadiyawa.feature_base.presentation.compose.composable.AppTextButton
import com.amadiyawa.feature_base.presentation.compose.composable.ButtonIconType
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButton
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.ErrorState
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.ProgressionIndicator
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextHeadlineLarge
import com.amadiyawa.feature_base.presentation.theme.dimension
import com.amadiyawa.feature_onboarding.domain.model.OnboardingScreen
import com.amadiyawa.feature_onboarding.presentation.components.getOnboardingImageSize
import com.amadiyawa.onboarding.R
import org.koin.androidx.compose.koinViewModel

/**
 * Composable function that displays the onboard list screen.
 *
 * This function sets up the scaffold and content for the onboard list screen,
 * including handling the UI state and displaying the appropriate content.
 */
@Composable
internal fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.uiStateFlow.collectAsState()
    val currentScreen = state.currentScreen
    val context = LocalContext.current

    // Collect one-time events
    LaunchedEffect(key1 = Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is OnboardingUiEvent.NavigateToAuth -> onFinished()
                is OnboardingUiEvent.ShowError -> {
                    // Show error message with a Snackbar
                    // You can replace this with your preferred error handling approach
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Loading indicator
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(visible = true)
            }
        } else if (currentScreen != null) {
            // Main content when data is loaded
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimension.spacing.medium),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Onboarding header with animated indicators
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.dimension.spacing.small)
                ) {
                    ProgressionIndicator(
                        currentLevel = state.currentScreenIndex,
                        totalLevels = state.screens.size - 1
                    )
                }

                // Onboarding content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OnboardingContent(screen = currentScreen)
                }

                // Bottom actions
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MaterialTheme.dimension.spacing.small)
                ) {
                    OnboardingActions(
                        isFirstScreen = state.isFirstScreen,
                        isLastScreen = state.isLastScreen,
                        onPrevious = { viewModel.dispatch(OnboardingAction.PreviousScreen) },
                        onNext = { viewModel.dispatch(OnboardingAction.NextScreen) },
                        onSkip = { viewModel.dispatch(OnboardingAction.SkipOnboarding) }
                    )
                }
            }
        } else if (state.error != null) {
            ErrorState(
                onRetry = { viewModel.dispatch(OnboardingAction.LoadScreens) },
                errorMessage = state.error
            )
        }
    }
}

@Composable
fun OnboardingContent(
    screen: OnboardingScreen,
    isLarge: Boolean = false
) {
    val imageSize = getOnboardingImageSize(isLarge)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image
        Image(
            painter = painterResource(id = screen.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(
                    width = imageSize.width.dp,
                    height = imageSize.height.dp
                )
                .padding(bottom = MaterialTheme.dimension.spacing.xLarge)
        )

        // Title
        TextHeadlineLarge(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = screen.titleResId),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.medium))

        // Description
        TextBodyLarge(
            text = stringResource(id = screen.descriptionResId),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnboardingActions(
    isFirstScreen: Boolean,
    isLastScreen: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {

    val backButtonColor by animateColorAsState(
        targetValue = if (!isFirstScreen) MaterialTheme.colorScheme.primary else
            MaterialTheme.colorScheme.surfaceVariant
    )

    val nextButtonColor by animateColorAsState(
        targetValue = if (!isLastScreen) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularButton(
            params = CircularButtonParams(
                iconType = ButtonIconType.Vector(Icons.AutoMirrored.Filled.ArrowBack),
                backgroundColor = backButtonColor,
                iconTint = MaterialTheme.colorScheme.onPrimary,
                onClick = onPrevious,
                description = stringResource(id = R.string.previous),
                enabled = !isFirstScreen
            )
        )

        Row {
            AnimatedContent(
                targetState = isLastScreen,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) { targetId ->
                if (targetId) {
                    FilledButton(
                        modifier = Modifier.height(MaterialTheme.dimension.componentSize.buttonLarge),
                        onClick = onSkip,
                        text = stringResource(id = R.string.get_started)
                    )
                } else {
                    AppTextButton(
                        onClick = onSkip,
                        text = stringResource(id = R.string.skip)
                    )
                }
            }
        }

        CircularButton(
            params = CircularButtonParams(
                iconType = ButtonIconType.Vector(Icons.AutoMirrored.Filled.ArrowForward),
                backgroundColor = nextButtonColor,
                iconTint = MaterialTheme.colorScheme.onPrimary,
                onClick = onNext,
                description = stringResource(id = R.string.next),
                enabled = !isLastScreen
            )
        )
    }
}