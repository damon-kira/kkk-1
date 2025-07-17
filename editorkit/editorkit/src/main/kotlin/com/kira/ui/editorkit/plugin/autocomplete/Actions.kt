

package com.kira.ui.editorkit.plugin.autocomplete

import com.kira.ui.editorkit.plugin.base.PluginContainer
import com.kira.ui.editorkit.plugin.base.PluginSupplier

var PluginContainer.suggestionAdapter: SuggestionAdapter?
    get() = findPlugin<AutoCompletePlugin>(AutoCompletePlugin.PLUGIN_ID)?.suggestionAdapter
    set(value) {
        findPlugin<AutoCompletePlugin>(AutoCompletePlugin.PLUGIN_ID)?.suggestionAdapter = value
    }

fun PluginSupplier.codeCompletion(block: AutoCompletePlugin.() -> Unit = {}) {
    plugin(AutoCompletePlugin().apply(block))
}