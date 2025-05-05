package com.amadiyawa.feature_auth.presentation.screen.resetpassword

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.domain.model.ResetPasswordForm
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
fun ResetPasswordScreen(
    onSuccess: (String) -> Unit,
) {
    val viewModel: ResetPasswordViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val uiEvent = viewModel.uiEvent.collectAsState(initial = null)

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch,
        viewModel = viewModel
    )

    uiEvent.value?.let { event ->
        when (event) {
            is ResetPasswordUiEvent.NavigateToSignIn -> {
                onSuccess(event.identifier)
            }

            is ResetPasswordUiEvent.ShowSnackbar -> {
                LaunchedEffect(Unit) {
                    Timber.e("Snackbar: ${event.message}")
                }
            }
        }
    }
}

@Composable
private fun SetupContent(
    state: ResetPasswordUiState,
    onAction: (ResetPasswordAction) -> Unit,
    viewModel: ResetPasswordViewModel
) {
    when (state) {
        is ResetPasswordUiState.Idle -> {
            ResetPasswordFormUI(
                form = state.form,
                onAction = onAction,
                viewModel = viewModel
            )
        }

        is ResetPasswordUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(visible = true)
            }
        }

        is ResetPasswordUiState.Error -> {
            ResetPasswordFormUI(
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
internal fun ResetPasswordFormUI(
    form: ResetPasswordForm,
    onAction: (ResetPasswordAction) -> Unit,
    viewModel: ResetPasswordViewModel
) {
    val isFormValid by remember(form) { derivedStateOf { form.asValidatedForm().isValid } }
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    FormScaffold {
        AuthHeader(
            title = stringResource(id = R.string.reset_password_title),
            description = stringResource(id = R.string.reset_password_description)
        )

        DefaultTextField(
            text = TextFieldText(
                value = form.password.value,
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password_placeholder),
                errorMessage = if (!form.password.validation.isValid) form.password.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(ResetPasswordAction.UpdateField("password", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(ResetPasswordAction.UpdateField("password", FieldValue.Text("")))
            },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        confirmPasswordFocusRequester.requestFocus()
                    }
                ),
                visualTransformation = if (form.password.isValueHidden) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIconConfig = TrailingIconConfig.Password(
                    text = form.password.value,
                    isVisible = form.password.isValueHidden
                )
            )
        )

        DefaultTextField(
            modifier = Modifier.focusRequester(confirmPasswordFocusRequester),
            text = TextFieldText(
                value = form.confirmPassword.value,
                label = stringResource(R.string.confirm_password),
                placeholder = stringResource(R.string.confirm_password_placeholder),
                errorMessage = if (!form.confirmPassword.validation.isValid) form.confirmPassword.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(ResetPasswordAction.UpdateField("confirmPassword", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(ResetPasswordAction.UpdateField("confirmPassword", FieldValue.Text("")))
            },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (form.confirmPassword.isValueHidden) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIconConfig = TrailingIconConfig.Password(
                    text = form.password.value,
                    isVisible = form.password.isValueHidden
                )
            )
        )

        FilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(Dimen.Size.extraLarge),
            text = stringResource(id = R.string.reset),
            onClick = { onAction(ResetPasswordAction.Submit) },
            enabled = isFormValid && !isSubmitting
        )
    }
}