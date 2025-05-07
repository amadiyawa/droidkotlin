package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.amadiyawa.feature_base.common.resources.Dimen
import com.amadiyawa.feature_base.presentation.theme.dimension
import com.amadiyawa.droidkotlin.base.R

/**
 * A reusable composable for displaying error states with retry functionality.
 * Supports localization through string resources.
 *
 * @param messageResId Resource ID for the error message. If null, a generic message will be shown.
 * @param errorMessage Optional direct error message string (used if messageResId is null)
 * @param onRetry Callback to be invoked when the retry button is clicked.
 * @param modifier Modifier to be applied to the composable.
 * @param titleResId Resource ID for the title text. Defaults to a generic error title.
 * @param retryButtonTextResId Resource ID for the retry button text.
 */
@Composable
fun ErrorState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    messageResId: Int? = null,
    errorMessage: String? = null,
    titleResId: Int = R.string.error_title,
    retryButtonTextResId: Int = R.string.error_retry
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimension.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextHeadlineSmall(
            text = stringResource(titleResId),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.small))

        // Prioritize resource ID if provided, fall back to direct string if available
        val message = when {
            messageResId != null -> stringResource(messageResId)
            errorMessage != null -> errorMessage
            else -> stringResource(R.string.error_unknown)
        }

        TextBodyMedium(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimension.spacing.large))

        FilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimen.Size.extraLarge),
            text = stringResource(retryButtonTextResId),
            onClick = onRetry
        )
    }
}