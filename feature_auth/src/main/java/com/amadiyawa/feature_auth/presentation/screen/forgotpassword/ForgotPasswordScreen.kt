package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.domain.model.ForgotPasswordForm
import com.amadiyawa.feature_auth.domain.model.VerificationResult
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.DefaultTextField
import com.amadiyawa.feature_base.presentation.compose.composable.FormScaffold
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingButton
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldConfig
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldText
import com.amadiyawa.feature_base.presentation.compose.composable.TrailingIconConfig
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
internal fun ForgotPasswordScreen(
    onOtpSent: (VerificationResult) -> Unit,
) {
    val viewModel: ForgotPasswordViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val events = viewModel.events.collectAsState(initial = null)

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch
    )

    events.value?.let { event ->
        when (event) {
            is ForgotPasswordUiEvent.NavigateToOtp -> {
                onOtpSent(event.data)
            }

            is ForgotPasswordUiEvent.ShowSnackbar -> {
                LaunchedEffect(Unit) {
                    Timber.e("Snackbar: ${event.message}")
                }
            }
        }
    }
}

@Composable
private fun SetupContent(
    state: ForgotPasswordUiState,
    onAction: (ForgotPasswordAction) -> Unit
) {
    val form = when (state) {
        is ForgotPasswordUiState.Idle -> state.form
        is ForgotPasswordUiState.Loading -> state.form
        is ForgotPasswordUiState.Error -> state.form
    }

    if (state is ForgotPasswordUiState.Error) {
        LaunchedEffect(state.message) {
            Timber.e("Form error: ${state.message}")
        }
    }

    ForgotPasswordFormUI(
        form = form,
        onAction = onAction,
        uiState = state
    )
}

@Composable
internal fun ForgotPasswordFormUI(
    form: ForgotPasswordForm,
    onAction: (ForgotPasswordAction) -> Unit,
    uiState: ForgotPasswordUiState
) {
    val isFormValid by remember(form) { derivedStateOf { form.asValidatedForm().isValid } }
    val forgotPasswordFocusRequester = remember { FocusRequester() }

    FormScaffold {
        AuthHeader(
            title = stringResource(id = R.string.forgot_password_title),
            description = stringResource(id = R.string.forgot_password_description)
        )

        DefaultTextField(
            text = TextFieldText(
                value = form.identifier.value,
                label = stringResource(R.string.identifier),
                placeholder = stringResource(R.string.forgot_password_placeholder),
                errorMessage = if (!form.identifier.validation.isValid) form.identifier.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(ForgotPasswordAction.UpdateField("identifier", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(ForgotPasswordAction.UpdateField("identifier", FieldValue.Text("")))
            },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        forgotPasswordFocusRequester.requestFocus()
                    }
                ),
                trailingIconConfig = TrailingIconConfig.Clearable("")
            )
        )

        LoadingButton(
            params = LoadingButtonParams(
                enabled = isFormValid,
                text = when (uiState) {
                    is ForgotPasswordUiState.Loading ->
                        stringResource(R.string.sending)
                    is ForgotPasswordUiState.Idle,
                    is ForgotPasswordUiState.Error -> // Not loading states
                        stringResource(id = R.string.validate)
                },
                isLoading = uiState is ForgotPasswordUiState.Loading,
                onClick = { onAction(ForgotPasswordAction.Submit) }
            )
        )
    }
}