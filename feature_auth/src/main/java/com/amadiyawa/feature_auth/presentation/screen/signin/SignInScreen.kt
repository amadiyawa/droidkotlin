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
import com.amadiyawa.feature_auth.domain.util.SocialProvider
import com.amadiyawa.feature_auth.presentation.components.SocialAuthFooter
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
import com.amadiyawa.feature_base.presentation.theme.dimension
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
    val context = LocalContext.current

    // Set default identifier if provided
    LaunchedEffect(defaultIdentifier) {
        defaultIdentifier?.takeIf { it.isNotEmpty() }?.let {
            viewModel.dispatch(
                SignInContract.Action.Initialize(defaultIdentifier)
            )
        }
    }

    // ✅ Proper event handling using LaunchedEffect + collect
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SignInContract.Event.NavigateToMainScreen -> {
                    onSignInSuccess()
                }

                is SignInContract.Event.NavigateToForgotPassword -> {
                    onForgotPassword()
                }

                is SignInContract.Event.ShowSnackbar -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignInContract.Event.SocialSignInResult -> {
                    // Handle social sign-in result
                    if (!event.success && event.message != null) {
                        Toast.makeText(
                            context,
                            event.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is SignInContract.Event.RequestFocus -> {
                    // Handle focus requests if needed
                    Timber.d("Request focus on field: ${event.field}")
                }

                is SignInContract.Event.HideKeyboard,
                is SignInContract.Event.ShowKeyboard -> {
                    // Handle keyboard visibility if needed
                }

                is SignInContract.Event.NavigateBack,
                is SignInContract.Event.NavigateToSignUp -> {
                    // Handle other navigation events if needed
                }
            }
        }
    }

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch
    )
}

@Composable
private fun SetupContent(
    state: SignInContract.State,
    onAction: (SignInContract.Action) -> Unit
) {
    val form = when (state) {
        is SignInContract.State.Idle -> state.form
        is SignInContract.State.Loading -> state.form
        is SignInContract.State.Error -> state.form
        is SignInContract.State.Success -> state.form
    }

    if (state is SignInContract.State.Error) {
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
    onAction: (SignInContract.Action) -> Unit,
    uiState: SignInContract.State
) {
    val passwordFocusRequester = remember { FocusRequester() }

    val isFormValid by remember(form) {
        derivedStateOf { form.canSubmit }
    }

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
                onAction(SignInContract.Action.UpdateField("identifier", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignInContract.Action.UpdateField("identifier", FieldValue.Text("")))
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
                onAction(SignInContract.Action.UpdateField("password", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignInContract.Action.UpdateField("password", FieldValue.Text("")))
            },
            onVisibilityChange = { onAction(SignInContract.Action.TogglePasswordVisibility) },
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
            onClick = { onAction(SignInContract.Action.ForgotPassword) },
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.align(Alignment.End)
        )

        // Loading button with dynamic text based on loading state
        LoadingButton(
            params = LoadingButtonParams(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(MaterialTheme.dimension.componentSize.buttonLarge),
                enabled = isFormValid,
                text = when (uiState) {
                    is SignInContract.State.Loading.Authentication ->
                        stringResource(R.string.signing_in)

                    is SignInContract.State.Loading.SocialAuthentication ->
                        stringResource(R.string.social_signing_in, uiState.provider.name.lowercase())

                    is SignInContract.State.Loading.SessionSaving ->
                        stringResource(R.string.saving_session)

                    is SignInContract.State.Loading.SessionActivation ->
                        stringResource(R.string.activating_session)

                    is SignInContract.State.Idle,
                    is SignInContract.State.Error,
                    is SignInContract.State.Success ->
                        stringResource(id = R.string.login)
                },
                isLoading = uiState is SignInContract.State.Loading,
                onClick = { onAction(SignInContract.Action.Submit) }
            )
        )

        // Bottom section: Social login options
        Spacer(modifier = Modifier.weight(1f))

        SocialAuthFooter(
            onGoogleSignIn = {
                onAction(SignInContract.Action.SocialSignIn(SocialProvider.GOOGLE))
            },
            onFacebookSignIn = {
                onAction(SignInContract.Action.SocialSignIn(SocialProvider.FACEBOOK))
            }
        )
    }
}