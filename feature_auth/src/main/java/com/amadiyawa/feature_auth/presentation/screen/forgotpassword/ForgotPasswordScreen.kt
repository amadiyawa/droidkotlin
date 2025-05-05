package com.amadiyawa.feature_auth.presentation.screen.forgotpassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.domain.model.ForgotPasswordForm
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.DefaultTextField
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.FormScaffold
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldConfig
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldText
import com.amadiyawa.feature_base.presentation.compose.composable.TrailingIconConfig
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
internal fun ForgotPasswordScreen(
    onOtpSent: (SignUp) -> Unit,
) {
    val viewModel: ForgotPasswordViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val uiEvent = viewModel.uiEvent.collectAsState(initial = null)

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch,
        viewModel = viewModel
    )

    uiEvent.value?.let { event ->
        when (event) {
            is ForgotPasswordUiEvent.NavigateToOtp -> {
                onOtpSent(event.signUp)
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
    onAction: (ForgotPasswordAction) -> Unit,
    viewModel: ForgotPasswordViewModel
) {
    when (state) {
        is ForgotPasswordUiState.Idle -> {
            ForgotPasswordFormUI(
                form = state.form,
                onAction = onAction,
                viewModel = viewModel
            )
        }

        is ForgotPasswordUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(visible = true)
            }
        }

        is ForgotPasswordUiState.Error -> {
            ForgotPasswordFormUI(
                form = state.form,
                onAction = onAction,
                viewModel = viewModel
            )
            LaunchedEffect(Unit) {
                Timber.e("Form error: ${state.message}")
            }
        }
    }
}

@Composable
internal fun ForgotPasswordFormUI(
    form: ForgotPasswordForm,
    onAction: (ForgotPasswordAction) -> Unit,
    viewModel: ForgotPasswordViewModel
) {
    val isFormValid by remember(form) { derivedStateOf { form.asValidatedForm().isValid } }
    val isSubmitting by viewModel.isSubmitting.collectAsState()
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

        FilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(forgotPasswordFocusRequester)
                .requiredHeight(Dimen.Size.extraLarge),
            enabled = isFormValid && !isSubmitting,
            text = stringResource(id = R.string.validate),
            onClick = { onAction(ForgotPasswordAction.Submit) }
        )
    }
}