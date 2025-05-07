package com.amadiyawa.feature_auth.presentation.screen.otpverification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.domain.model.OtpForm
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.presentation.compose.composable.AppTextButton
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.FormConfig
import com.amadiyawa.feature_base.presentation.compose.composable.FormScaffold
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingButton
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyMedium
import com.amadiyawa.feature_base.presentation.compose.composable.otpFieldColors
import com.amadiyawa.feature_base.presentation.theme.dimension
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.Locale

@Composable
internal fun OtpVerificationScreen(
    data: VerificationResult,
    onOtpValidated: () -> Unit,
    onResetPassword: (resetToken: String) -> Unit,
    onCancel: () -> Unit,
) {
    Timber.d("OtpVerificationScreen: $data")

    val viewModel: OtpVerificationViewModel = koinViewModel {
        parametersOf(data)
    }

    val uiState by viewModel.uiStateFlow.collectAsState()

    // Process UI events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is OtpUiEvent.NavigateToHome -> {
                    onOtpValidated()
                }
                is OtpUiEvent.NavigateToResetPassword -> {
                    onResetPassword(event.resetToken)
                }
                is OtpUiEvent.ShowError -> {
                    // Show error using a Snackbar or Toast
                }
                is OtpUiEvent.ClearError -> {
                    // Clear any displayed errors
                }
                is OtpUiEvent.UpdateResendAvailability -> {
                    // This is handled implicitly through state observation
                }
            }
        }
    }

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch,
        onCancel = onCancel
    )
}

@Composable
private fun SetupContent(
    state: OtpUiState,
    onAction: (OtpAction) -> Unit,
    onCancel: () -> Unit
) {
    if (state is OtpUiState.Error) {
        LaunchedEffect(state.errorMessage) {
            Timber.e("Form error: ${state.errorMessage}")
        }
    }

    OtpFormUI(
        form = state.form,
        errorMessage = state.errorMessage,
        onAction = onAction,
        state = state,
        onCancel = onCancel
    )
}

@Composable
private fun OtpFormUI(
    form: OtpForm,
    errorMessage: String?,
    onAction: (OtpAction) -> Unit,
    state: OtpUiState,
    onCancel: () -> Unit
) {
    val (emailOrPhone, description) = if (form.recipient.contains("@")) {
        "e-mails" to stringResource(R.string.otp_description_email, form.recipient)
    } else {
        "sms" to stringResource(R.string.otp_description_phone, form.recipient)
    }

    FormScaffold(
        config = FormConfig(onBackPressed = onCancel)
    ) {
        AuthHeader(
            title = stringResource(R.string.otp_title, emailOrPhone),
            description = description,
            centerContent = true
        )

        OtpFormInput(
            otpForm = form,
            errorMessage = errorMessage,
            onDigitChanged = { index, value ->
                onAction(OtpAction.UpdateDigit(index, value))
            }
        )

        ResendSection(
            form = form,
            onResend = { onAction(OtpAction.ResendOtp) }
        )

        LoadingButton(
            params = LoadingButtonParams(
                enabled = form.isComplete && form.isValid,
                text = when (state) {
                    is OtpUiState.Loading ->
                        stringResource(R.string.verifying)
                    is OtpUiState.Idle,
                    is OtpUiState.Error -> // Not loading states
                        stringResource(id = R.string.verify)
                },
                isLoading = state.isLoading,
                onClick = { onAction(OtpAction.Submit) }
            )
        )
    }
}

@Composable
fun OtpFormInput(
    otpForm: OtpForm,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onDigitChanged: (index: Int, value: String) -> Unit
) {
    val focusRequesters = remember { List(otpForm.digits.size) { FocusRequester() } }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OtpFieldsRow(
            otpForm = otpForm,
            focusRequesters = focusRequesters,
            onDigitChanged = onDigitChanged
        )

        AnimatedVisibility(visible = !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OtpFieldsRow(
    otpForm: OtpForm,
    focusRequesters: List<FocusRequester>,
    onDigitChanged: (index: Int, value: String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        otpForm.digits.forEachIndexed { index, field ->
            OtpField(
                index = index,
                field = field,
                focusRequesters = focusRequesters,
                onDigitChanged = onDigitChanged
            )
        }
    }
}

@Composable
private fun OtpField(
    index: Int,
    field: ValidatedField<String>,
    focusRequesters: List<FocusRequester>,
    onDigitChanged: (index: Int, value: String) -> Unit
) {
    OutlinedTextField(
        value = field.value,
        onValueChange = { value ->
            if (value.length <= 1) {
                onDigitChanged(index, value)
                if (value.length == 1 && index < focusRequesters.lastIndex) {
                    focusRequesters[index + 1].requestFocus()
                }
            }
        },
        modifier = Modifier
            .requiredWidth(48.dp)
            .requiredHeight(56.dp)
            .focusRequester(focusRequesters[index])
            .onKeyEvent { handleKeyEvent(it, index, field, focusRequesters, onDigitChanged) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        isError = field.isTouched && !field.validation.isValid,
        colors = otpFieldColors(field)
    )
}

private fun handleKeyEvent(
    event: KeyEvent,
    index: Int,
    field: ValidatedField<String>,
    focusRequesters: List<FocusRequester>,
    onDigitChanged: (index: Int, value: String) -> Unit
): Boolean {
    return if (
        event.type == KeyEventType.KeyDown &&
        event.key == Key.Backspace &&
        field.value.isEmpty() &&
        index > 0
    ) {
        focusRequesters[index - 1].requestFocus()
        onDigitChanged(index - 1, "")
        true
    } else {
        false
    }
}

@Composable
private fun ResendSection(
    form: OtpForm,
    onResend: () -> Unit
) {
    val canResend = form.resendState is ResendState.Available
    val countdown = when (val state = form.resendState) {
        is ResendState.Countdown -> state.secondsRemaining
        else -> null
    }

    val minutes = countdown?.div(60) ?: 0
    val seconds = countdown?.rem(60) ?: 0

    val formattedMinutes = String.format(Locale.getDefault(), "%02d", minutes)
    val formattedSeconds = String.format(Locale.getDefault(), "%02d", seconds)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = MaterialTheme.dimension.spacing.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            TextBodyMedium(
                text = stringResource(R.string.didnt_receive_code),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimension.spacing.small))

            AppTextButton(
                text = stringResource(R.string.resend),
                onClick = onResend,
                enabled = canResend
            )
        }

        AnimatedVisibility(
            visible = countdown != null && countdown > 0,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            countdown?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = MaterialTheme.dimension.spacing.small)
                ) {
                    TextBodyLarge(text = " " + stringResource(R.string.resend_timer_separator) + " ")
                    TextBodyLarge(text = "$formattedMinutes:$formattedSeconds")
                }
            }
        }
    }
}
