

package com.kira.ui.feature.shortcuts.domain.repository

import com.kira.ui.feature.shortcuts.domain.model.Keybinding

interface ShortcutsRepository {

    suspend fun loadShortcuts(): List<Keybinding>
    suspend fun restoreDefaults()

    suspend fun reassign(keybinding: Keybinding)
    suspend fun disable(keybinding: Keybinding)
}