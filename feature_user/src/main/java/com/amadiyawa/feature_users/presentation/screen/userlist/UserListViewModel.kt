package com.amadiyawa.feature_users.presentation.screen.userlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import com.amadiyawa.feature_users.domain.model.User
import com.amadiyawa.feature_users.domain.usecase.GetUserListUseCase
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModel.Action
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModel.UiState
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModel.UiState.Content
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModel.UiState.Error
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModel.UiState.Loading
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

internal class UserListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getUserListUseCase: GetUserListUseCase,
) : BaseViewModel<UiState, Action>(Loading) {

    private var currentPage = 1

    fun onEnter() {
        getUserList()
    }

    private var job: Job? = null

    private fun getUserList() {
        if (job != null) {
            job?.cancel()
            job = null
        }

        job = viewModelScope.launch {
            getUserListUseCase(currentPage, 10).also { result ->
                val action = when (result) {
                    is Result.Success -> {
                        if (result.value.isEmpty()) {
                            Action.UserListLoadFailure
                        } else {
                            Timber.tag("UserListViewModel").d("getUserList: %s", result.value)
                            Action.UserListLoadSuccess(result.value)
                        }
                    }
                    is Result.Failure -> {
                        Action.UserListLoadFailure
                    }

                    is Result.Exception -> TODO()
                }
                sendAction(action)
                if (result is Result.Success) {
                    currentPage++
                }
            }
        }
    }

    internal sealed interface Action : BaseAction<UiState> {
        data class UserListLoadSuccess(private val newUsers: List<User>) : Action {
            override fun reduce(state: UiState): UiState {
                return if (state is Content) {
                    // If the current state is Content, append the new users to the existing list
                    Content(state.users + newUsers)
                } else {
                    // If the current state is not Content, replace the state with the new users
                    Content(newUsers)
                }
            }
        }

        data object UserListLoadFailure : Action {
            override fun reduce(state: UiState) = Error
        }
    }

    @Immutable
    internal sealed interface UiState : BaseState {
        data class Content(val users: List<User>) : UiState
        data object Loading : UiState
        data object Error : UiState
    }
}
