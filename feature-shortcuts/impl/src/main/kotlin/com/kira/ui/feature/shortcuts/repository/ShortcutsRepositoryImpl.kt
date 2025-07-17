

package com.kira.ui.feature.shortcuts.repository

import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.shortcuts.domain.model.Keybinding
import com.kira.ui.feature.shortcuts.domain.model.Shortcut
import com.kira.ui.feature.shortcuts.domain.repository.ShortcutsRepository
import kotlinx.coroutines.withContext

class ShortcutsRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val settingsManager: SettingsManager,
) : ShortcutsRepository {

    override suspend fun loadShortcuts(): List<Keybinding> {
        return withContext(dispatcherProvider.io()) {
            Shortcut.values().map { keybinding ->
                val value = settingsManager.load(
                    key = keybinding.key,
                    defaultValue = keybinding.defaultValue,
                )
                Keybinding(
                    shortcut = keybinding,
                    isCtrl = value[0] == '1',
                    isShift = value[1] == '1',
                    isAlt = value[2] == '1',
                    key = value[3],
                )
            }
        }
    }

    override suspend fun restoreDefaults() {
        withContext(dispatcherProvider.io()) {
            Shortcut.values().forEach { keybinding ->
                settingsManager.remove(keybinding.key)
            }
        }
    }

    override suspend fun reassign(keybinding: Keybinding) {
        withContext(dispatcherProvider.io()) {
            val value = StringBuilder().apply {
                val isValid = keybinding.isCtrl || keybinding.isAlt
                if (keybinding.isCtrl) append('1') else append('0')
                if (keybinding.isShift) append('1') else append('0')
                if (keybinding.isAlt) append('1') else append('0')
                if (isValid) append(keybinding.key) else append('\u0000')
            }
            settingsManager.update(keybinding.shortcut.key, value.toString())
        }
    }

    override suspend fun disable(keybinding: Keybinding) {
        withContext(dispatcherProvider.io()) {
            val value = StringBuilder().apply {
                append('0') // ctrl
                append('0') // shift
                append('0') // alt
                append('\u0000') // none
            }
            settingsManager.update(keybinding.shortcut.key, value.toString())
        }
    }
}