package com.amadiyawa.feature_auth.presentation.screen.signup

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.amadiyawa.feature_auth.R
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.domain.model.SignUpForm
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.common.util.getCountryDialCode
import com.amadiyawa.feature_base.domain.model.FieldValue
import com.amadiyawa.feature_base.presentation.compose.composable.AuthHeader
import com.amadiyawa.feature_base.presentation.compose.composable.DefaultTextField
import com.amadiyawa.feature_base.presentation.compose.composable.FilledButton
import com.amadiyawa.feature_base.presentation.compose.composable.FormScaffold
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TermsAndConditions
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldConfig
import com.amadiyawa.feature_base.presentation.compose.composable.TextFieldText
import com.amadiyawa.feature_base.presentation.compose.composable.TrailingIconConfig
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SignUpScreen(
    onSignUpSuccess: (SignUp) -> Unit,
) {
    val viewModel: SignUpViewModel = koinViewModel()
    val uiState by viewModel.uiStateFlow.collectAsState()
    val uiEvent = viewModel.uiEvent.collectAsState(initial = null)

    SetupContent(
        state = uiState,
        onAction = viewModel::dispatch,
        viewModel = viewModel
    )

    uiEvent.value?.let { event ->
        when (event) {
            is SignUpUiEvent.NavigateToOtp -> onSignUpSuccess(event.signUp)
            is SignUpUiEvent.ShowSnackbar -> {
                LaunchedEffect(Unit) {
                    Timber.e("Snackbar: ${event.message}")
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun SetupContent(
    state: SignUpUiState,
    onAction: (SignUpAction) -> Unit,
    viewModel: SignUpViewModel
) {
    when (state) {
        is SignUpUiState.Idle -> {
            SignUpFormUI(
                form = state.form,
                onAction = onAction,
                viewModel = viewModel
            )
        }

        is SignUpUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(visible = true)
            }
        }

        is SignUpUiState.Error -> {
            SignUpFormUI(
                form = state.form,
                onAction = onAction,
                viewModel = viewModel
            )
            LaunchedEffect(Unit) {
                // Affiche une erreur via Snackbar, Toast, etc.
                Timber.e("Form error: ${state.message}")
            }
        }
    }
}

@Composable
internal fun SignUpFormUI(
    form: SignUpForm,
    onAction: (SignUpAction) -> Unit,
    viewModel: SignUpViewModel
) {
    val phonePrefix = remember { getCountryDialCode() }

    val usernameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    val isFormValid by remember(form) {
        derivedStateOf { form.asValidatedForm().isValid }
    }

    val isSubmitting by viewModel.isSubmitting.collectAsState()

    FormScaffold {
        AuthHeader(
            title = stringResource(id = R.string.signup_title),
            description = stringResource(id = R.string.signup_description)
        )

        DefaultTextField(
            text = TextFieldText(
                value = form.fullName.value,
                label = stringResource(R.string.fullname),
                placeholder = stringResource(R.string.fullname_placeholder),
                errorMessage = if (!form.fullName.validation.isValid) form.fullName.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(SignUpAction.UpdateField("fullName", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignUpAction.UpdateField("fullName", FieldValue.Text("")))
            },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        usernameFocusRequester.requestFocus()
                    }
                )
            )
        )

        DefaultTextField(
            modifier = Modifier.focusRequester(usernameFocusRequester),
            text = TextFieldText(
                value = form.username.value,
                label = stringResource(R.string.username),
                placeholder = stringResource(R.string.username_placeholder),
                errorMessage = if (!form.username.validation.isValid) form.username.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(SignUpAction.UpdateField("username", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignUpAction.UpdateField("username", FieldValue.Text("")))
            },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        emailFocusRequester.requestFocus()
                    }
                )
            )
        )

        DefaultTextField(
            modifier = Modifier.focusRequester(emailFocusRequester),
            text = TextFieldText(
                value = form.email.value,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.email_placeholder),
                errorMessage = if (!form.email.validation.isValid) form.email.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(SignUpAction.UpdateField("email", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignUpAction.UpdateField("email", FieldValue.Text("")))
            },
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        phoneFocusRequester.requestFocus()
                    }
                )
            )
        )

        DefaultTextField(
            modifier = Modifier.focusRequester(phoneFocusRequester),
            text = TextFieldText(
                value = form.phone.value,
                label = stringResource(R.string.phone),
                placeholder = stringResource(R.string.phone_placeholder),
                errorMessage = if (!form.phone.validation.isValid) form.phone.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(SignUpAction.UpdateField("phone", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignUpAction.UpdateField("phone", FieldValue.Text("")))
            },
            leadingBadge = phonePrefix,
            config = TextFieldConfig(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
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
                value = form.password.value,
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.password_placeholder),
                errorMessage = if (!form.password.validation.isValid) form.password.validation.errorMessage else null,
            ),
            onValueChange = {
                onAction(SignUpAction.UpdateField("password", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignUpAction.UpdateField("password", FieldValue.Text("")))
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
                trailingIconConfig = TrailingIconConfig(
                    isPasswordField = true,
                    isPasswordVisible = form.password.isValueHidden
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
                onAction(SignUpAction.UpdateField("confirmPassword", FieldValue.Text(it)))
            },
            onClearText = {
                onAction(SignUpAction.UpdateField("confirmPassword", FieldValue.Text("")))
            },
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
                visualTransformation = if (form.confirmPassword.isValueHidden) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIconConfig = TrailingIconConfig(
                    isPasswordField = true,
                    isPasswordVisible = form.confirmPassword.isValueHidden
                )
            )
        )

        TermsAndConditions(
            isChecked = form.termsAccepted.value,
            onCheckedChange = { accepted ->
                onAction(
                    SignUpAction.UpdateField("termsAccepted", FieldValue.BooleanValue(accepted))
                )
            }
        )

        FilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(Dimen.Size.extraLarge),
            text = stringResource(id = R.string.register),
            onClick = { onAction(SignUpAction.Submit) },
            enabled = isFormValid && !isSubmitting
        )
    }
}