

package com.kira.ui.editorkit.plugin.textscroller

import android.util.Log
import com.kira.ui.editorkit.plugin.base.EditorPlugin
import com.kira.ui.editorkit.widget.TextProcessor
import com.kira.ui.editorkit.widget.TextScroller

class TextScrollerPlugin : EditorPlugin(PLUGIN_ID) {

    var scroller: TextScroller? = null

    override fun onAttached(editText: TextProcessor) {
        super.onAttached(editText)
        scroller?.attachTo(editText)
        Log.d(PLUGIN_ID, "TextScroller plugin loaded successfully!")
    }

    override fun onDetached(editText: TextProcessor) {
        super.onDetached(editText)
        scroller?.detach()
        scroller = null
    }

    companion object {
        const val PLUGIN_ID = "text-scroller-1821"
    }
}