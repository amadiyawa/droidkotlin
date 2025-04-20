package com.amadiyawa.feature_users.presentation.screen.userlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.screen.viewmodel.OldBaseAction
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.screen.viewmodel.OldBaseViewModel
import com.amadiyawa.feature_users.domain.model.User
import com.amadiyawa.feature_users.domain.usecase.GetUserListUseCase
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModelOld.ActionOld
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModelOld.UiState
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModelOld.UiState.Content
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModelOld.UiState.Error
import com.amadiyawa.feature_users.presentation.screen.userlist.UserListViewModelOld.UiState.Loading
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

internal class UserListViewModelOld(
    private val savedStateHandle: SavedStateHandle,
    private val getUserListUseCase: GetUserListUseCase,
) : OldBaseViewModel<UiState, ActionOld>(Loading) {

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
                    is OperationResult.Success -> {
                        if (result.data.isEmpty()) {
                            ActionOld.UserListLoadFailure
                        } else {
                            Timber.tag("UserListViewModel").d("getUserList: %s", result.data)
                            ActionOld.UserListLoadSuccess(result.data)
                        }
                    }
                    is OperationResult.Failure -> {
                        ActionOld.UserListLoadFailure
                    }

                    is OperationResult.Error -> TODO()
                }
                sendAction(action)
                if (result is OperationResult.Success) {
                    currentPage++
                }
            }
        }
    }

    internal sealed interface ActionOld : OldBaseAction<UiState> {
        data class UserListLoadSuccess(private val newUsers: List<User>) : ActionOld {
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

        data object UserListLoadFailure : ActionOld {
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
