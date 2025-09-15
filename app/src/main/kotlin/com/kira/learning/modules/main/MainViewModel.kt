package com.kira.learning.modules.main

import com.kira.learning.base.keyvalue.SettingsManager
import com.kira.learning.base.mvi.BaseApiViewModel
import com.kira.learning.base.mvi.ViewEvent
import com.kira.learning.models.ColorScheme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface MainEvent : ViewEvent {
    object LoadTheme : MainEvent
    object ToggleFullscreen : MainEvent
    data class UpdateColorScheme(val colorScheme: ColorScheme) : MainEvent
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
) : BaseApiViewModel<MainViewState, MainEvent>(
    initialState = MainViewState(
        fullscreenMode = settingsManager.fullScreenMode
    )
) {

    init {
        handleAction(MainEvent.LoadTheme)
    }

    override fun handleAction(action: MainEvent) {
        when (action) {
            is MainEvent.LoadTheme -> loadTheme()
            is MainEvent.ToggleFullscreen -> toggleFullscreen()
            is MainEvent.UpdateColorScheme -> updateColorScheme(action.colorScheme)
        }
    }

    override fun updateLoadingState(isLoading: Boolean, message: String) {
        updateState { copy(isLoading = isLoading) }
    }

    private fun loadTheme() {
        updateState {
            copy(
                // 先简单设置为 null，因为需要从 String 转换为 ColorScheme 的逻辑
                colorScheme = null,
                fullscreenMode = settingsManager.fullScreenMode
            )
        }
    }

    private fun toggleFullscreen() {
        val newMode = !currentState.fullscreenMode
        settingsManager.fullScreenMode = newMode
        updateState { copy(fullscreenMode = newMode) }
    }

    private fun updateColorScheme(colorScheme: ColorScheme) {
        // 这里需要将 ColorScheme 转换为 String 保存到 settings
        // 具体的转换逻辑取决于项目的设计
        updateState { copy(colorScheme = colorScheme) }
    }
}