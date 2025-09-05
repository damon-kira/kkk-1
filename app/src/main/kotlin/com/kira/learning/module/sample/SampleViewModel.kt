package com.kira.learning.module.sample

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kira.learning.base.mvi.BaseUiState
import com.kira.learning.base.mvi.BaseViewModel
import com.kira.learning.base.mvi.UiEvent
import com.kira.learning.base.mvi.ViewState
import com.kira.learning.model.SampleImage
import com.kira.learning.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// 使用通用的BaseUiState替代原有的SampleUiState
data class SampleViewState(
    val data: BaseUiState<List<SampleImage>> = BaseUiState.Idle
) : ViewState()

// 使用通用的事件系统
sealed interface SampleEvent {
    object LoadSamples : SampleEvent
    object RefreshSamples : SampleEvent
    object RetrySamples : SampleEvent
}

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val repo: SampleRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<SampleViewState, UiEvent>(
    initialState = SampleViewState()
) {

    init {
        handleAction(SampleEvent.LoadSamples)
    }

    override fun handleAction(action: Any) {
        when (action) {
            is SampleEvent.LoadSamples -> loadSamples(refresh = false)
            is SampleEvent.RefreshSamples -> loadSamples(refresh = true)
            is SampleEvent.RetrySamples -> loadSamples(refresh = false)
        }
    }

    private fun loadSamples(refresh: Boolean) {
        viewModelScope.launch {
            val currentData = currentState.data
            updateState {
                copy(
                    data = when {
                        refresh && currentData is BaseUiState.Success ->
                            currentData.copy(refreshing = true)

                        else -> BaseUiState.Loading
                    }
                )
            }

            when (val result = repo.load(force = refresh)) {
                is ApiResult.Success -> updateState {
                    copy(data = BaseUiState.Success(result.data, refreshing = false))
                }

                is ApiResult.Error -> {
                    updateState { copy(data = BaseUiState.Error(result.message)) }
                    sendEvent(UiEvent.ShowSnackbar(result.message))
                }

                ApiResult.NetworkUnavailable -> {
                    val message = "网络不可用"
                    updateState { copy(data = BaseUiState.Error(message)) }
                    sendEvent(UiEvent.ShowSnackbar(message))
                }
            }
        }
    }
}
