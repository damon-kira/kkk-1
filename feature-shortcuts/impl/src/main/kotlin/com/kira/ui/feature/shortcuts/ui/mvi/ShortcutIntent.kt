

package com.kira.ui.feature.shortcuts.ui.mvi

import com.kira.ui.core.mvi.ViewIntent
import com.kira.ui.feature.shortcuts.domain.model.Keybinding

sealed class ShortcutIntent : ViewIntent() {

    data object LoadShortcuts : ShortcutIntent()
    data object RestoreDefaults : ShortcutIntent()

    data class Reassign(val keybinding: Keybinding) : ShortcutIntent()
    data class ResolveConflict(val reassign: Boolean) : ShortcutIntent()
}