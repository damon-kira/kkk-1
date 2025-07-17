

package com.kira.ui.editorkit.plugin.shortcuts

import com.kira.ui.editorkit.plugin.base.PluginContainer
import com.kira.ui.editorkit.plugin.base.PluginSupplier

var PluginContainer.onShortcutListener: OnShortcutListener?
    get() = findPlugin<ShortcutsPlugin>(ShortcutsPlugin.PLUGIN_ID)?.onShortcutListener
    set(value) {
        findPlugin<ShortcutsPlugin>(ShortcutsPlugin.PLUGIN_ID)?.onShortcutListener = value
    }

fun PluginSupplier.shortcuts(block: ShortcutsPlugin.() -> Unit = {}) {
    plugin(ShortcutsPlugin().apply(block))
}