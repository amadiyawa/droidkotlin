package com.amadiyawa.feature_onboarding.presentation.screen.onboardlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.compose.composable.AppOutlinedButton
import com.amadiyawa.feature_base.presentation.compose.composable.AppTextButton
import com.amadiyawa.feature_base.presentation.compose.composable.DataNotFoundAnim
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextHeadlineLarge
import com.amadiyawa.feature_onboarding.domain.model.Onboard
import com.amadiyawa.onboarding.R
import org.koin.androidx.compose.koinViewModel

/**
 * This is the entry point for the Onboarding feature.
 * Here you can find the complete Onboarding Screen
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
 * Arrange the whole UI-content with a padding value
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
 * Handle different UI 
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
    onboarding: OnboardViewModel.UiState.Onboarding,
    viewModel: OnboardViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimen.Padding.screenContent)
    ) {
        Onboard(
            onboarding = onboarding,
            viewModel = viewModel
        )
    }
}

@Composable
private fun OnboardHeader(
    modifier: Modifier = Modifier,
    viewModel: OnboardViewModel,
    onboard: Onboard,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimen.Size.extraLarge),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = onboard.id != 0
        ) {
            Icon(
                modifier = Modifier.clickable { viewModel.previousOnboard() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.skip),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Skip Button
        AppTextButton(
            onClick = { viewModel.skipOnboarding() },
            text = stringResource(id = R.string.skip),
            textDecoration = TextDecoration.Underline

        )
    }
}

@Composable
private fun Onboard(
    onboarding: OnboardViewModel.UiState.Onboarding,
    viewModel: OnboardViewModel
) {
    val onboard = onboarding.currentOnboard
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF9BC1D3)) // Outer background color
    ) {

        // Background in White with complete Size
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        ) {
            // Empty block as this is only for background decoration
        }

        // Overlay your main content in this Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimen.Padding.screenContent), // Adjust padding as needed for content
            verticalArrangement = Arrangement.Center, // Vertically center the content
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardHeader(
                modifier = Modifier.fillMaxHeight(0.03f),
                onboard = onboard,
                viewModel = viewModel
            )

            // Main Content Area - 70% of screen height
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.73f) // Takes up 70% of the screen height
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Image
                Image(
                    painter = painterResource(id = onboard.imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(200.dp) // Adjust as needed
                        .padding(16.dp)
                )

                // Title
                TextHeadlineLarge(
                    modifier = Modifier
                        .fillMaxWidth() //All size
                        .padding(horizontal = 24.dp), // Adjust padding on the right and left side
                    text = stringResource(id = onboard.titleResId),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                // A bit of Space between title and Description
                Spacer(modifier = Modifier.height(10.dp))

                // Description
                TextBodyLarge(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(id = onboard.descriptionResId),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center // Center the text
                )
            }

            // Bottom Navigation Buttons - 20% of screen height
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // Adjusting the height based on the current Onboard Screen
                    .fillMaxHeight(if (onboarding.currentOnboard.id == 0) 0.55f else 0.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                //Button next
                ButtonNextOnboard(viewModel = viewModel)

                //Button previous hidden- Only show Previous button if not on the first page
                if (onboarding.currentOnboard.id > 0) {
                    ButtonPreviousOnboard(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun ButtonNextOnboard(viewModel: OnboardViewModel) {
    FilledButton(
        text = stringResource(id = R.string.next),
        onClick = { viewModel.nextOnboard() }
    )
}

@Composable
private fun ButtonPreviousOnboard(viewModel: OnboardViewModel) {
    AppOutlinedButton(
        text = stringResource(id = R.string.previous),
        onClick = { viewModel.previousOnboard() }
    )
}
