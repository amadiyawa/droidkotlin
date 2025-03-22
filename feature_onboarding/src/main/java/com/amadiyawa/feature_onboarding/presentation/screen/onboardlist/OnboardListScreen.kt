package com.amadiyawa.feature_onboarding.presentation.screen.onboardlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.presentation.compose.composable.AppTextButton
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButton
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.DataNotFoundAnim
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.ProgressionIndicator
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextHeadlineLarge
import com.amadiyawa.feature_base.presentation.theme.dimension
import com.amadiyawa.onboarding.R
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

/**
 * Composable function that displays the onboard list screen.
 *
 * This function sets up the scaffold and content for the onboard list screen,
 * including handling the UI state and displaying the appropriate content.
 */
@Composable
fun OnboardListScreen() {
    val viewModel: OnboardViewModel = koinViewModel()
    viewModel.onEnter()

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        SetupContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
        )
    }
}

/**
 * Sets up the content for the onboard list screen.
 *
 * This function arranges the content within a column, applying the provided padding values
 * and handling the UI state using the provided view model.
 *
 * @param paddingValues The padding values to be applied to the content.
 * @param viewModel The view model for the onboard list screen.
 */
@Composable
private fun SetupContent(
    paddingValues: PaddingValues,
    viewModel: OnboardViewModel
) {
    val uiState: OnboardViewModel.UiState = viewModel.uiStateFlow.collectAsStateWithLifecycle().value

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ){
        HandleUiState(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

/**
 * Handles the UI state for the onboard list screen.
 *
 * This function takes the current UI state and the view model, and displays
 * the appropriate content based on the state.
 *
 * @param uiState The current UI state of the onboard list screen.
 * @param viewModel The view model for the onboard list screen.
 */
@Composable
private fun HandleUiState(
    uiState: OnboardViewModel.UiState,
    viewModel: OnboardViewModel
) {
    uiState.let {
        when (it) {
            OnboardViewModel.UiState.Error -> {
                DataNotFoundAnim()
            }
            OnboardViewModel.UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingAnimation(visible = true)
                }
            }
            is OnboardViewModel.UiState.Onboarding -> {
                Onboarding(
                    onboarding = it,
                    viewModel = viewModel
                )
            }

            OnboardViewModel.UiState.OnboardingComplete -> TODO()
        }
    }
}

@Composable
private fun Onboarding(
    modifier: Modifier = Modifier,
    onboarding: OnboardViewModel.UiState.Onboarding,
    viewModel: OnboardViewModel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimension.gridTwo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimension.gridTwo)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.gridTwo)
        ) {
            OnboardHeader(
                modifier = Modifier,
                onboarding = onboarding
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Onboard(
                modifier = Modifier,
                onboarding = onboarding
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.gridTwo)
        ) {
            OnboardFooter(
                modifier = Modifier,
                onboarding = onboarding,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun Onboard(
    modifier: Modifier = Modifier,
    onboarding: OnboardViewModel.UiState.Onboarding
) {
    val onboard = onboarding.currentOnboard
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(onboarding.isNext, onboard.id) {
        val targetValue = if (onboarding.isNext) 1000f else -1000f
        offsetX.animateTo(
            targetValue = targetValue,
            animationSpec = tween(durationMillis = 0)
        )
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) },
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.gridTwo, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image
        Image(
            painter = painterResource(id = onboard.imageResId),
            contentDescription = null,
            modifier = Modifier.height(MaterialTheme.dimension.windowWidthCompactOneThird)
        )

        // Title
        TextHeadlineLarge(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = onboard.titleResId),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        // Description
        TextBodyLarge(
            text = stringResource(id = onboard.descriptionResId),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Composable function that displays the header for the onboarding screen.
 *
 * @param modifier A [Modifier] to be applied to the header.
 * @param onboarding The current onboarding state.
 */
@Composable
private fun OnboardHeader(
    modifier: Modifier = Modifier,
    onboarding: OnboardViewModel.UiState.Onboarding
) {
    val onboard = onboarding.currentOnboard
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgressionIndicator(
            currentLevel = onboard.id,
            totalLevels = onboarding.onboardList.lastIndex
        )
    }
}

/**
 * Composable function that displays the footer for the onboarding screen.
 *
 * @param modifier A [Modifier] to be applied to the footer.
 * @param onboarding The current onboarding state.
 * @param viewModel The view model for the onboard list screen.
 */
@Composable
private fun OnboardFooter(
    modifier: Modifier = Modifier,
    onboarding: OnboardViewModel.UiState.Onboarding,
    viewModel: OnboardViewModel
) {
    val onboard = onboarding.currentOnboard
    val backButtonColor by animateColorAsState(
        targetValue = if (onboard.id != 0) MaterialTheme.colorScheme.primary else
            MaterialTheme.colorScheme.surfaceVariant
    )
    val nextButtonColor by animateColorAsState(
        targetValue = if (onboard.id != onboarding.onboardList.size - 1) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(MaterialTheme.dimension.gridTwo)
    ) {
        // Previous button aligned to the start
        CircularButton(
            circularButtonParams = CircularButtonParams(
                onClick = { viewModel.previousOnboard() },
                enabled = onboard.id != 0,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                description = stringResource(id = R.string.previous)
            ),
            modifier = Modifier.align(Alignment.CenterStart),
            color = backButtonColor,
            onColor = MaterialTheme.colorScheme.onPrimary
        )

        // Skip or Get Started button aligned to the center
        Row(modifier = Modifier.align(Alignment.Center)) {
            AnimatedContent(
                targetState = onboard.id,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) { targetId ->
                if (targetId != onboarding.onboardList.size - 1) {
                    AppTextButton(
                        onClick = { viewModel.skipOnboarding() },
                        text = stringResource(id = R.string.skip)
                    )
                } else {
                    FilledButton(
                        modifier = Modifier.heightIn(MaterialTheme.dimension.gridSix),
                        onClick = { viewModel.skipOnboarding() },
                        text = stringResource(id = R.string.get_started)
                    )
                }
            }
        }

        // Next button aligned to the end
        CircularButton(
            circularButtonParams = CircularButtonParams(
                onClick = { viewModel.nextOnboard() },
                enabled = onboard.id != onboarding.onboardList.size - 1,
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                description = stringResource(id = R.string.next)
            ),
            modifier = Modifier.align(Alignment.CenterEnd),
            color = nextButtonColor,
            onColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}