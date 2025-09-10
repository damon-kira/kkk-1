package com.kira.learning.module.main

import androidx.lifecycle.ViewModel
import com.kira.learning.base.keyvalue.SettingsManager
import com.kira.learning.base.mvi.ViewEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _viewState = MutableStateFlow(initialViewState())
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    private val _viewEvent = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvent: Flow<ViewEvent> = _viewEvent.receiveAsFlow()

    init {
//        loadTheme()
//        registerOnPreferenceChangeListeners()
    }

    override fun onCleared() {
        super.onCleared()
//        unregisterOnPreferenceChangeListeners()
    }

    private fun initialViewState(): MainViewState {
        return MainViewState(
            isLoading = true,
            colorScheme = null,
            fullscreenMode = settingsManager.fullScreenMode,
        )
    }
}