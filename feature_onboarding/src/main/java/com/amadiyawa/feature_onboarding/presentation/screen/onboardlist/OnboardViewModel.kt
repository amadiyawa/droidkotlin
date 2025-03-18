package comß.amadiyawa.feature_onboarding.presentation.screen.onboardlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import com.amadiyawa.feature_onboarding.domain.usecase.GetOnboardListUseCase
import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_onboarding.domain.model.Onboard
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

internal class OnboardViewModel(
    private val getOnboardListUseCase: GetOnboardListUseCase
) : BaseViewModel<OnboardViewModel.UiState, OnboardViewModel.Action>(UiState.Loading) {
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
                    is Result.Success -> {
                        if (result.value.isEmpty()) {
                            Action.OnboardListLoadFailure
                        } else {
                            Action.OnboardListLoadSuccess(result.value)
                        }
                    }
                    is Result.Failure -> {
                        Action.OnboardListLoadFailure
                    }
                }
                sendAction(action)
            }
        }
    }

    fun nextOnboard() {
        sendAction(Action.NextOnboard)
    }

    fun previousOnboard() {
        sendAction(Action.PreviousOnboard)
    }

    fun skipOnboarding() {
        sendAction(Action.SkipOnboarding)
    }

    internal sealed interface Action : BaseAction<UiState> {
        data class OnboardListLoadSuccess(private val onboardList: List<Onboard>) : Action {
            override fun reduce(state: UiState) = UiState.Onboarding(onboardList)
        }

        data object OnboardListLoadFailure : Action {
            override fun reduce(state: UiState) = UiState.Error
        }

        data object NextOnboard : Action {
            override fun reduce(state: UiState): UiState {
                return if (state is UiState.Onboarding && state.currentOnboardIndex < state.onboardList.size - 1) {
                    state.copy(currentOnboardIndex = state.currentOnboardIndex + 1)
                } else {
                    UiState.OnboardingComplete
                }
            }
        }

        data object PreviousOnboard : Action {
            override fun reduce(state: UiState): UiState {
                return if (state is UiState.Onboarding && state.currentOnboardIndex > 0) {
                    state.copy(currentOnboardIndex = state.currentOnboardIndex - 1)
                } else {
                    state // Return the current state if the condition isn't met
                }
            }
        }

        data object SkipOnboarding : Action {
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
            val currentOnboardIndex : Int = 0
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