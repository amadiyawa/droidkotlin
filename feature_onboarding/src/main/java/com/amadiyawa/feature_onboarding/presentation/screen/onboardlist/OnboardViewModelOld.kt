package com.amadiyawa.feature_onboarding.presentation.screen.onboardlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.presentation.screen.viewmodel.OldBaseAction
import com.amadiyawa.feature_base.presentation.screen.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.screen.viewmodel.OldBaseViewModel
import com.amadiyawa.feature_onboarding.domain.usecase.GetOnboardListUseCase
import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_onboarding.domain.model.Onboard
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

internal class OnboardViewModelOld(
    private val getOnboardListUseCase: GetOnboardListUseCase
) : OldBaseViewModel<OnboardViewModelOld.UiState, OnboardViewModelOld.ActionOld>(UiState.Loading) {
    private var job: Job? = null

    fun onEnter() {
        getOnboardList()
    }

    private fun getOnboardList() {
        if (job != null) {
            job?.cancel()
            job = null
        }

        job = viewModelScope.launch {
            getOnboardListUseCase().also { result ->
                Timber.d("getQuestionListUseCase result: $result")
                val action = when (result) {
                    is OperationResult.Success -> {
                        if (result.data.isEmpty()) {
                            ActionOld.OnboardListLoadFailure
                        } else {
                            ActionOld.OnboardListLoadSuccess(result.data)
                        }
                    }
                    is OperationResult.Failure -> {
                        ActionOld.OnboardListLoadFailure
                    }

                    is OperationResult.Error -> ActionOld.OnboardListLoadFailure
                }
                sendAction(action)
            }
        }
    }

    fun nextOnboard() {
        sendAction(ActionOld.NextOnboard)
    }

    fun previousOnboard() {
        sendAction(ActionOld.PreviousOnboard)
    }

    fun skipOnboarding() {
        sendAction(ActionOld.SkipOnboarding)
    }

    internal sealed interface ActionOld : OldBaseAction<UiState> {
        data class OnboardListLoadSuccess(private val onboardList: List<Onboard>) : ActionOld {
            override fun reduce(state: UiState) = UiState.Onboarding(onboardList)
        }

        data object OnboardListLoadFailure : ActionOld {
            override fun reduce(state: UiState) = UiState.Error
        }

        data object NextOnboard : ActionOld {
            override fun reduce(state: UiState): UiState {
                return if (state is UiState.Onboarding && state.currentOnboardIndex < state.onboardList.size - 1) {
                    state.copy(
                        currentOnboardIndex = state.currentOnboardIndex + 1,
                        isNext = true
                    )
                } else {
                    UiState.OnboardingComplete
                }
            }
        }

        data object PreviousOnboard : ActionOld {
            override fun reduce(state: UiState): UiState {
                return if (state is UiState.Onboarding && state.currentOnboardIndex > 0) {
                    state.copy(
                        currentOnboardIndex = state.currentOnboardIndex - 1,
                        isNext = false
                    )
                } else {
                    state // Return the current state if the condition isn't met
                }
            }
        }

        data object SkipOnboarding : ActionOld {
            override fun reduce(state: UiState): UiState {
                return UiState.OnboardingComplete
            }
        }
    }

    @Immutable
    internal sealed interface UiState : BaseState {
        data object Loading : UiState
        data class Onboarding(
            val onboardList: List<Onboard>,
            val currentOnboardIndex : Int = 0,
            val isNext: Boolean = true
        ) : UiState {
            val currentOnboard: Onboard
                get() = onboardList[currentOnboardIndex]
        }
        data object Error : UiState
        data object OnboardingComplete : UiState
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}