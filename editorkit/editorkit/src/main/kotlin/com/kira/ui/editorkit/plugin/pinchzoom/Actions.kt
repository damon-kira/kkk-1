

package com.kira.ui.editorkit.plugin.pinchzoom

import com.kira.ui.editorkit.plugin.base.PluginSupplier

fun PluginSupplier.pinchZoom(block: PinchZoomPlugin.() -> Unit = {}) {
    plugin(PinchZoomPlugin().apply(block))
}