

package com.kira.ui.editorkit.plugin.base

class PluginSupplier private constructor() {

    private val plugins = mutableSetOf<EditorPlugin>()

    fun <T : EditorPlugin> plugin(plugin: T) {
        plugins.add(plugin)
    }

    fun supply(): Set<EditorPlugin> {
        return plugins
    }

    companion object {

        fun create(block: PluginSupplier.() -> Unit): PluginSupplier {
            return PluginSupplier().apply(block)
        }
    }
}