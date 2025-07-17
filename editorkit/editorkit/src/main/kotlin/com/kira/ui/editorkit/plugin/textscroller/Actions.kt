

package com.kira.ui.editorkit.plugin.textscroller

import com.kira.ui.editorkit.plugin.base.PluginSupplier

fun PluginSupplier.textScroller(block: TextScrollerPlugin.() -> Unit = {}) {
    plugin(TextScrollerPlugin().apply(block))
}