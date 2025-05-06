package com.amadiyawa.feature_auth.presentation.screen.otpverification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.domain.model.OtpForm
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.amadiyawa.feature_base.presentation.compose.composable.AppTextButton
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.FormScaffold
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyLarge
import com.amadiyawa.feature_base.presentation.compose.composable.otpFieldColors
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun OtpVerificationScreen(
    data: VerificationResult,
    onOtpValidated: (data: VerificationResult) -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel: OtpVerificationViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val uiEvent = viewModel.uiEvent.collectAsState(initial = null)

    LaunchedEffect(data) {
        viewModel.dispatch(OtpAction.Initialize(data))
    }

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch,
        viewModel = viewModel
    )

    uiEvent.value?.let { event ->
        when (event) {
            is OtpUiEvent.NavigateToNextScreen -> onOtpValidated(event.data)
        }
    }
}

@Composable
private fun SetupContent(
    state: OtpUiState,
    onAction: (OtpAction) -> Unit,
    viewModel: OtpVerificationViewModel
) {
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    when (state) {
        is OtpUiState.Idle -> {
            OtpFormUI(
                form = state.form,
                isSubmitting = isSubmitting,
                errorMessage = null,
                onAction = onAction,
                viewModel = viewModel
            )
        }

        is OtpUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(visible = true)
            }
        }

        is OtpUiState.Error -> {
            OtpFormUI(
                form = state.form,
                isSubmitting = isSubmitting,
                errorMessage = state.message,
                onAction = onAction,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun OtpFormUI(
    form: OtpForm,
    isSubmitting: Boolean,
    errorMessage: String?,
    onAction: (OtpAction) -> Unit,
    viewModel: OtpVerificationViewModel
) {
    val verificationResult = viewModel.verificationResult.collectAsStateWithLifecycle()
    val (emailOrPhone, description) = if (verificationResult.value.recipient.contains("@")) {
        "e-mails" to stringResource(R.string.otp_description_email, verificationResult.value.recipient)
    } else {
        "sms" to stringResource(R.string.otp_description_phone, verificationResult.value.recipient)
    }

    FormScaffold {
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

        OtpResendField(verificationResult)

        FilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(Dimen.Size.extraLarge),
            text = stringResource(id = R.string.verify),
            onClick = {  onAction(OtpAction.Submit) },
            enabled = form.isFullyFilled() && !isSubmitting
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
private fun OtpResendField(
    signUp: State<VerificationResult>
) {
    val minutes = signUp.value.expiresIn / 60
    val seconds = signUp.value.expiresIn % 60

    val formattedMinutes = String.format("%02d", minutes)
    val formattedSeconds = String.format("%02d", seconds)
    Row {
        AppTextButton(
            text = stringResource(R.string.resend),
            onClick = { /* TODO: Handle resend OTP */ },
            enabled = signUp.value.expiresIn <= 0
        )
        AnimatedVisibility(visible = signUp.value.expiresIn > 0) {
            Row {
                TextBodyLarge(text = " " + stringResource(R.string.resend_timer_separator) + " ")
                TextBodyLarge(text = "$formattedMinutes:$formattedSeconds")
            }
        }
    }
}