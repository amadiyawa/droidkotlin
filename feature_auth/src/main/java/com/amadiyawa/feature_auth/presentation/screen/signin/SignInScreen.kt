package com.amadiyawa.feature_auth.presentation.screen.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.presentation.screen.welcome.AuthFooter
import com.amadiyawa.feature_auth.presentation.state.AuthStatus
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.presentation.compose.composable.AppTextButton
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.DefaultTextField
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldConfig
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldText
import com.amadiyawa.feature_base.presentation.compose.composable.Toolbar
import com.amadiyawa.feature_base.presentation.compose.composable.TrailingIconConfig
import com.amadiyawa.feature_base.presentation.theme.dimension
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SignInScreen(
    viewModel: SignInViewModelOld = koinViewModel(),
    onSignInSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    viewModel.onEnter()

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            Toolbar(
                centered = true,
                backgroundColor = MaterialTheme.colorScheme.background
            )
        }
    ) { paddingValues ->
        SetupContent(
            viewModel = viewModel,
            onSignInSuccess = onSignInSuccess,
            onForgotPassword = onForgotPassword,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun SetupContent(
    viewModel: SignInViewModelOld,
    onSignInSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    paddingValues: PaddingValues
) {
    val uiState: SignInViewModelOld.UiState = viewModel.uiStateFlow.collectAsStateWithLifecycle().value

    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = MaterialTheme.dimension.gridTwo)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .imePadding()
        ) {
            uiState.let {
                when (it) {
                    is SignInViewModelOld.UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingAnimation(visible = true)
                        }
                    }
                    is SignInViewModelOld.UiState.Form -> {
                        when ((uiState as SignInViewModelOld.UiState.Form).status) {
                            is AuthStatus.Authenticated -> {
                                onSignInSuccess()
                            }

                            is AuthStatus.Invalid -> {
                                if (uiState.status is AuthStatus.Invalid) {
                                    val msg = uiState.status.message ?: "Invalid credentials"
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }

                            is AuthStatus.Error -> {
                                Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()
                            }

                            else -> {}
                        }

                        SignInContent(
                            viewModel = viewModel,
                            onSignInSuccess = onSignInSuccess,
                            onForgotPassword = onForgotPassword
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SignInContent(
    viewModel: SignInViewModelOld,
    onSignInSuccess: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
) {
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    val form = viewModel.form.collectAsStateWithLifecycle()
    val validatedForm = viewModel.validatedForm.collectAsStateWithLifecycle()
    val isSubmitting = viewModel.isSubmitting.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimension.gridThree,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthHeader(
            title = stringResource(id = R.string.welcome_back),
            description = stringResource(id = R.string.signin_description)
        )

        Spacer(modifier = Modifier.weight(1f))

        DefaultTextField(
            text = TextFieldText(
                value = form.value.identifier.value,
                label = stringResource(R.string.identifier),
                placeholder = stringResource(R.string.identifier_placeholder),
                errorMessage = if (!form.value.identifier.validation.isValid) form.value.identifier.validation.errorMessage else null,
            ),
            onValueChange = { viewModel.onIdentifierChanged(it) },
            onClearText = { viewModel.onIdentifierClear() },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                )
            )
        )

        DefaultTextField(
            modifier = Modifier.focusRequester(passwordFocusRequester),
            text = TextFieldText(
                label = stringResource(R.string.password),
                value = form.value.password.value,
                placeholder = stringResource(R.string.password_placeholder),
                errorMessage = if (!form.value.password.validation.isValid) form.value.password.validation.errorMessage else null,
            ),
            onValueChange = { viewModel.onPasswordChanged(it) },
            onClearText = {  },
            onVisibilityChange = { viewModel.onPasswordVisibilityToggle() },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                visualTransformation = if(form.value.password.isValueHidden) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIconConfig = TrailingIconConfig(
                    isPasswordField = true,
                    isPasswordVisible = form.value.password.isValueHidden
                )
            )
        )

        AppTextButton(
            text = stringResource(R.string.forgot_password),
            onClick = { onForgotPassword() },
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.align(Alignment.End)
        )

        FilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(Dimen.Size.extraLarge),
            enabled = validatedForm.value.isValid && !isSubmitting.value,
            text = stringResource(id = R.string.login),
            onClick = {  }
        )

        Spacer(modifier = Modifier.weight(1f))

        AuthFooter()
    }
}