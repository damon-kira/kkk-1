

package com.kira.ui.editorkit.plugin.delimiters

import com.kira.ui.editorkit.plugin.base.PluginSupplier

fun PluginSupplier.highlightDelimiters(block: BracketsHighlightPlugin.() -> Unit = {}) {
    plugin(BracketsHighlightPlugin().apply(block))
}