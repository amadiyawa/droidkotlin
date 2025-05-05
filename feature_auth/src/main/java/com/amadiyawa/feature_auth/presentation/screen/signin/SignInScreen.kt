package com.amadiyawa.feature_auth.presentation.screen.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.domain.model.SignInForm
import com.amadiyawa.feature_auth.presentation.screen.welcome.AuthFooter
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.compose.composable.AppTextButton
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
internal fun SignInScreen(
    defaultIdentifier: String?,
    onSignInSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    val viewModel: SignInViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val events = viewModel.events.collectAsState(initial = null)

    // Set default identifier if provided
    LaunchedEffect(defaultIdentifier) {
        defaultIdentifier?.takeIf { it.isNotEmpty() }?.let {
            viewModel.dispatch(SignInAction.UpdateField("identifier", FieldValue.Text(it)))
        }
    }

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch
    )

    events.value?.let { event ->
        when (event) {
            is SignInUiEvent.NavigateToMainScreen -> {
                onSignInSuccess()
            }

            is SignInUiEvent.NavigateToForgotPassword -> {
                onForgotPassword()
            }

            is SignInUiEvent.ShowSnackbar -> {
                Toast.makeText(
                    LocalContext.current,
                    event.snackbarMessage.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is SignInUiEvent.SocialSignInResult -> {
                // Handle social sign-in result if needed
                if (!event.success && event.message != null) {
                    Toast.makeText(
                        LocalContext.current,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
private fun SetupContent(
    state: SignInUiState,
    onAction: (SignInAction) -> Unit
) {
    val form = when (state) {
        is SignInUiState.Idle -> state.form
        is SignInUiState.Loading -> state.form
        is SignInUiState.Error -> state.form
    }

    if (state is SignInUiState.Error) {
        LaunchedEffect(state.message) {
            Timber.e("Form error: ${state.message}")
        }
    }

    SignInFormUI(
        form = form,
        onAction = onAction,
        uiState = state
    )
}

@Composable
internal fun SignInFormUI(
    form: SignInForm,
    onAction: (SignInAction) -> Unit,
    uiState: SignInUiState
) {
    val passwordFocusRequester = remember { FocusRequester() }
    val isFormValid by remember(form) { derivedStateOf { form.asValidatedForm().isValid } }

    FormScaffold {
        AuthHeader(
            title = stringResource(id = R.string.welcome_back),
            description = stringResource(id = R.string.signin_description)
        )

        DefaultTextField(
            text = TextFieldText(
                value = form.identifier.value,
                label = stringResource(R.string.identifier),
                placeholder = stringResource(R.string.identifier_placeholder),
                errorMessage = if (!form.identifier.validation.isValid) form.identifier.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(SignInAction.UpdateField("identifier", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignInAction.UpdateField("identifier", FieldValue.Text("")))
            },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                ),
                trailingIconConfig = TrailingIconConfig.Clearable("")
            )
        )

        DefaultTextField(
            modifier = Modifier.focusRequester(passwordFocusRequester),
            text = TextFieldText(
                value = form.password.value,
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password_placeholder),
                errorMessage = if (!form.password.validation.isValid) form.password.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(SignInAction.UpdateField("password", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignInAction.UpdateField("password", FieldValue.Text("")))
            },
            onVisibilityChange = { onAction(SignInAction.TogglePasswordVisibility) },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if (form.password.isValueHidden) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIconConfig = TrailingIconConfig.Password(
                    text = form.password.value,
                    isVisible = form.password.isValueHidden
                )
            )
        )

        AppTextButton(
            text = stringResource(R.string.forgot_password),
            onClick = { onAction(SignInAction.ForgotPassword) },
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.align(Alignment.End)
        )

        // Loading button with dynamic text based on loading state
        LoadingButton(
            params = LoadingButtonParams(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(Dimen.Size.extraLarge),
                enabled = isFormValid,
                text = when (uiState) {
                    is SignInUiState.Loading.Authentication ->
                        stringResource(R.string.signing_in)

                    is SignInUiState.Loading.SocialAuthentication ->
                        stringResource(R.string.social_signing_in, uiState.provider.name.lowercase())

                    is SignInUiState.Loading.SessionSaving ->
                        stringResource(R.string.saving_session)

                    is SignInUiState.Loading.SessionActivation ->
                        stringResource(R.string.activating_session)
                    is SignInUiState.Idle,
                    is SignInUiState.Error -> // Not loading states
                        stringResource(id = R.string.login)
                },
                isLoading = uiState is SignInUiState.Loading,
                onClick = { onAction(SignInAction.Submit) }
            )
        )

        // Bottom section: Social login options
        Spacer(modifier = Modifier.weight(1f))

        AuthFooter()
    }
}