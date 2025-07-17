

package com.kira.ui.editorkit.plugin.autoindent

import com.kira.ui.editorkit.plugin.base.PluginSupplier

fun PluginSupplier.autoIndentation(block: AutoIndentPlugin.() -> Unit = {}) {
    plugin(AutoIndentPlugin().apply(block))
}