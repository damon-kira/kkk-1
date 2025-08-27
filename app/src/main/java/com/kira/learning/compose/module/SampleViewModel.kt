package com.kira.learning.compose.module

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val repo: SampleRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SampleUiState>(SampleUiState.Idle)
    val uiState: StateFlow<SampleUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        dispatch(SampleEvent.Load)
    }

    fun dispatch(event: SampleEvent) {
        when (event) {
            SampleEvent.Load -> load(force = false, refresh = false)
            SampleEvent.Refresh -> load(force = true, refresh = true)
            is SampleEvent.Retry -> load(force = true, refresh = false)
        }
    }

    private fun load(force: Boolean, refresh: Boolean) {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            val current = _uiState.value
            _uiState.value = when {
                refresh && current is SampleUiState.Success -> current.copy(refreshing = true)
                else -> SampleUiState.Loading
            }
            val result = repo.load(force)
            _uiState.value = result.fold(
                onSuccess = { data -> SampleUiState.Success(data, refreshing = false) },
                onFailure = { SampleUiState.Error(it.message ?: "未知错误") }
            )
        }
    }
}
