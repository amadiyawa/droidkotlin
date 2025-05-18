package com.amadiyawa.feature_profile.presentation.screen.profilemain

import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.errorhandling.ErrorHandler
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseViewModel
import com.amadiyawa.feature_profile.domain.usecase.CheckUserPermissionsUseCase
import com.amadiyawa.feature_profile.domain.usecase.GetUserProfileUseCase
import com.amadiyawa.feature_profile.domain.usecase.RefreshUserProfileUseCase
import com.amadiyawa.feature_profile.domain.usecase.SignOutUserUseCase

class ProfileMainViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val checkUserPermissionsUseCase: CheckUserPermissionsUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    errorHandler: ErrorHandler
) : BaseViewModel<ProfileMainContract.State, ProfileMainContract.Action>(
    initialState = ProfileMainContract.State(),
    errorHandler = errorHandler
) {

    init {
        dispatch(ProfileMainContract.Action.LoadUserProfile)
    }

    override fun dispatch(action: ProfileMainContract.Action) {
        logAction(action)

        when (action) {
            ProfileMainContract.Action.LoadUserProfile -> loadUserProfile()
            ProfileMainContract.Action.RefreshUserProfile -> refreshUserProfile()
            ProfileMainContract.Action.EditProfileClicked -> navigateToEditProfile()
            ProfileMainContract.Action.SettingsClicked -> navigateToSettings()
            ProfileMainContract.Action.AboutClicked -> navigateToAbout()
            ProfileMainContract.Action.SignOutClicked -> signOutUser()
        }
    }

    private fun loadUserProfile() {
        setState { it.copy(isLoading = true, error = null) }

        launchSafely {
            // Load profile data
            val profileResult = getUserProfileUseCase()

            when (profileResult) {
                is OperationResult.Success -> {
                    val profileData = profileResult.data

                    // Load permissions
                    val permissionsResult = checkUserPermissionsUseCase()

                    when (permissionsResult) {
                        is OperationResult.Success -> {
                            val permissions = permissionsResult.data
                            setState { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    userName = profileData.userName,
                                    userEmail = profileData.userEmail,
                                    userPhone = profileData.userPhone,
                                    userAvatarUrl = profileData.userAvatarUrl,
                                    userRole = profileData.userRole,
                                    hasEditProfileAccess = permissions.hasEditProfileAccess,
                                    hasSettingsAccess = permissions.hasSettingsAccess,
                                    hasSignOutAccess = permissions.hasSignOutAccess,
                                    error = null
                                )
                            }
                        }
                        else -> {
                            // Still show profile data but with default permissions
                            setState { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    userName = profileData.userName,
                                    userEmail = profileData.userEmail,
                                    userPhone = profileData.userPhone,
                                    userAvatarUrl = profileData.userAvatarUrl,
                                    userRole = profileData.userRole,
                                    error = "Could not load permissions"
                                )
                            }
                            handleOperationError(permissionsResult)
                        }
                    }
                }
                else -> {
                    setState { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = errorHandler?.getLocalizedMessage(profileResult) ?: "Failed to load profile"
                        )
                    }
                    emitEvent(ProfileMainContract.Effect.ShowError(
                        errorHandler?.getLocalizedMessage(profileResult) ?: "Failed to load profile"
                    ))
                    handleOperationError(profileResult)
                }
            }
        }
    }

    private fun refreshUserProfile() {
        launchSafely {
            val result = refreshUserProfileUseCase()

            handleResult(
                result = result,
                onSuccess = { profileData ->
                    setState { currentState ->
                        currentState.copy(
                            userName = profileData.userName,
                            userEmail = profileData.userEmail,
                            userPhone = profileData.userPhone,
                            userAvatarUrl = profileData.userAvatarUrl,
                            userRole = profileData.userRole,
                            error = null
                        )
                    }
                    emitEvent(ProfileMainContract.Effect.ShowMessage("Profile refreshed"))
                },
                onError = { message ->
                    emitEvent(ProfileMainContract.Effect.ShowError(message))
                },
                customErrorHandling = true
            )
        }
    }

    private fun navigateToEditProfile() {
        if (state.hasEditProfileAccess) {
            emitEvent(ProfileMainContract.Effect.NavigateToEditProfile)
        } else {
            emitEvent(ProfileMainContract.Effect.ShowError("You don't have permission to edit profile"))
        }
    }

    private fun navigateToSettings() {
        emitEvent(ProfileMainContract.Effect.NavigateToSettings)
    }

    private fun navigateToAbout() {
        emitEvent(ProfileMainContract.Effect.NavigateToAbout)
    }

    private fun signOutUser() {
        launchSafely {
            val result = signOutUserUseCase()

            handleResult(
                result = result,
                onSuccess = {
                    emitEvent(ProfileMainContract.Effect.UserSignedOut)
                },
                onError = { message ->
                    emitEvent(ProfileMainContract.Effect.ShowError(message))
                },
                customErrorHandling = true
            )
        }
    }
}