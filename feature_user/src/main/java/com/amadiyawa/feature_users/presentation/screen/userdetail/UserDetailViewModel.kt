package com.amadiyawa.feature_users.presentation.screen.userdetail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import com.amadiyawa.feature_users.domain.model.User
import com.amadiyawa.feature_users.domain.usecase.GetUserUseCase
import com.amadiyawa.feature_users.presentation.screen.userdetail.UserDetailViewModel.Action
import com.amadiyawa.feature_users.presentation.screen.userdetail.UserDetailViewModel.UiState
import com.amadiyawa.feature_users.presentation.screen.userdetail.UserDetailViewModel.UiState.Content
import kotlinx.coroutines.launch

internal class UserDetailViewModel(
    private val getUserDetailUseCase: GetUserUseCase,
) : BaseViewModel<UiState, Action>(UiState.Loading) {

    fun onEnter(uuid: String) {
        getUserByUuid(uuid)
    }

    private fun getUserByUuid(uuid: String) {
        viewModelScope.launch {
            getUserDetailUseCase(uuid).also { result ->
                val action = when (result) {
                    is OperationResult.Success -> {
                        Action.UserDetailSuccess(result.data)
                    }
                    is OperationResult.Failure -> Action.UserDetailFailure
                    is OperationResult.Error -> TODO()
                }
                sendAction(action)
            }
        }
    }

    internal sealed interface Action : BaseAction<UiState> {
        class UserDetailSuccess(private val user: User) : Action {
            override fun reduce(state: UiState) = Content(user)
        }

        data object UserDetailFailure : Action {
            override fun reduce(state: UiState) = UiState.Error
        }
    }

    @Immutable
    internal sealed interface UiState : BaseState {
        data class Content(val user: User) : UiState
        data object Loading : UiState
        data object Error : UiState
    }
}