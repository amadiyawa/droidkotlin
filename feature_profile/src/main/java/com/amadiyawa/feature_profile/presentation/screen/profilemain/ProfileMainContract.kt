package com.amadiyawa.feature_profile.presentation.screen.profilemain

import com.amadiyawa.feature_base.domain.util.UserRole
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState

/**
 * Contract for ProfileMain screen to manage state, actions and effects
 */
object ProfileMainContract {

    /**
     * Represents the UI state for the ProfileMain screen
     */
    data class State(
        // User data
        val userName: String = "",
        val userEmail: String? = "",
        val userPhone: String = "",
        val userAvatarUrl: String? = null,
        val userRole: UserRole = UserRole.NONE,

        // Screen state
        val isLoading: Boolean = true,
        val error: String? = null,

        // Available features
        val hasEditProfileAccess: Boolean = false,
        val hasSettingsAccess: Boolean = true,
        val hasSignOutAccess: Boolean = true
    ) : BaseState

    /**
     * Actions that can be triggered from the UI
     */
    sealed class Action {
        // Navigation actions
        object EditProfileClicked : Action()
        object SettingsClicked : Action()
        object AboutClicked : Action()
        object SignOutClicked : Action()

        // Data loading actions
        object LoadUserProfile : Action()
        object RefreshUserProfile : Action()
    }

    /**
     * Side effects triggered by the ViewModel
     */
    sealed class Effect {
        object NavigateToEditProfile : Effect()
        object NavigateToSettings : Effect()
        object NavigateToAbout : Effect()
        object UserSignedOut : Effect()
        data class ShowError(val message: String) : Effect()
        data class ShowMessage(val message: String) : Effect()
    }
}