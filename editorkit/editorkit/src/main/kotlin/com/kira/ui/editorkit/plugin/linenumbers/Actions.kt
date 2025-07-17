

package com.kira.ui.editorkit.plugin.linenumbers

import com.kira.ui.editorkit.plugin.base.PluginSupplier

fun PluginSupplier.lineNumbers(block: LineNumbersPlugin.() -> Unit = {}) {
    plugin(LineNumbersPlugin().apply(block))
}