package com.kira.learning.xml.modules.supereditor.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.core.storage.keyvalue.SettingsManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
class SuperEditorViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _viewEvent = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvent: Flow<ViewEvent> = _viewEvent.receiveAsFlow()

    val fullScreenMode: Boolean
        get() = settingsManager.fullScreenMode
    val confirmExit: Boolean
        get() = settingsManager.confirmExit

    fun handleIntent(intent: Intent?) {
        viewModelScope.launch {
            if (intent != null) {
                _viewEvent.send(ViewEvent.NewIntent(intent))
            }
        }
    }
}