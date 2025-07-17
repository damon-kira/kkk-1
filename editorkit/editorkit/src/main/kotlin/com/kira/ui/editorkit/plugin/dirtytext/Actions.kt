

package com.kira.ui.editorkit.plugin.dirtytext

import com.kira.ui.editorkit.plugin.base.PluginContainer
import com.kira.ui.editorkit.plugin.base.PluginSupplier

var PluginContainer.onChangeListener: OnChangeListener?
    get() = findPlugin<DirtyTextPlugin>(DirtyTextPlugin.PLUGIN_ID)?.onChangeListener
    set(value) {
        findPlugin<DirtyTextPlugin>(DirtyTextPlugin.PLUGIN_ID)?.onChangeListener = value
    }

fun PluginSupplier.changeDetector(block: DirtyTextPlugin.() -> Unit = {}) {
    plugin(DirtyTextPlugin().apply(block))
}