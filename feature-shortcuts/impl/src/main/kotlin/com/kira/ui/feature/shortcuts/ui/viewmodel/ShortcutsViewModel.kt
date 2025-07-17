

package com.kira.ui.feature.shortcuts.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.core.provider.resources.StringProvider
import com.kira.ui.feature.shortcuts.domain.model.Keybinding
import com.kira.ui.feature.shortcuts.domain.repository.ShortcutsRepository
import com.kira.ui.feature.shortcuts.ui.mvi.ShortcutIntent
import com.kira.ui.feature.shortcuts.ui.navigation.ShortcutScreen
import com.kira.ui.uikit.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShortcutsViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val shortcutsRepository: ShortcutsRepository,
) : ViewModel() {

    private val _shortcuts = MutableStateFlow<List<Keybinding>>(emptyList())
    val shortcuts: StateFlow<List<Keybinding>> = _shortcuts.asStateFlow()

    private val _viewEvent = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvent: Flow<ViewEvent> = _viewEvent.receiveAsFlow()

    private var pendingKey: Keybinding? = null
    private var conflictKey: Keybinding? = null

    init {
        loadShortcuts()
    }

    fun obtainEvent(event: ShortcutIntent) {
        when (event) {
            is ShortcutIntent.LoadShortcuts -> loadShortcuts()
            is ShortcutIntent.RestoreDefaults -> restoreDefaults()

            is ShortcutIntent.Reassign -> reassignShortcut(event)
            is ShortcutIntent.ResolveConflict -> resolveConflict(event)
        }
    }

    private fun loadShortcuts() {
        viewModelScope.launch {
            try {
                _shortcuts.value = shortcutsRepository.loadShortcuts()
            } catch (e: Exception) {
                Timber.e(e, e.message)
                _viewEvent.send(
                    ViewEvent.Toast(stringProvider.getString(R.string.common_error_occurred)),
                )
            }
        }
    }

    private fun restoreDefaults() {
        viewModelScope.launch {
            try {
                shortcutsRepository.restoreDefaults()
                loadShortcuts()
            } catch (e: Exception) {
                Timber.e(e, e.message)
                _viewEvent.send(
                    ViewEvent.Toast(stringProvider.getString(R.string.common_error_occurred)),
                )
            }
        }
    }

    private fun reassignShortcut(event: ShortcutIntent.Reassign) {
        viewModelScope.launch {
            try {
                val existingKey = shortcuts.value.find {
                    it.shortcut != event.keybinding.shortcut &&
                        it.key == event.keybinding.key &&
                        it.isCtrl == event.keybinding.isCtrl &&
                        it.isShift == event.keybinding.isShift &&
                        it.isAlt == event.keybinding.isAlt
                }
                if (existingKey != null) {
                    pendingKey = event.keybinding
                    conflictKey = existingKey
                    _viewEvent.send(ViewEvent.Navigation(ShortcutScreen.Conflict()))
                } else {
                    shortcutsRepository.reassign(event.keybinding)
                    loadShortcuts()
                }
            } catch (e: Exception) {
                Timber.e(e, e.message)
                _viewEvent.send(
                    ViewEvent.Toast(stringProvider.getString(R.string.common_error_occurred)),
                )
            }
        }
    }

    private fun resolveConflict(event: ShortcutIntent.ResolveConflict) {
        viewModelScope.launch {
            try {
                if (event.reassign) {
                    shortcutsRepository.disable(checkNotNull(conflictKey))
                    shortcutsRepository.reassign(checkNotNull(pendingKey))
                }
                pendingKey = null
                conflictKey = null
                loadShortcuts()
            } catch (e: Exception) {
                Timber.e(e, e.message)
                _viewEvent.send(
                    ViewEvent.Toast(stringProvider.getString(R.string.common_error_occurred)),
                )
            }
        }
    }
}