

package com.kira.ui.editorkit.plugin.dirtytext

import android.text.Editable
import android.util.Log
import com.kira.ui.editorkit.plugin.base.EditorPlugin
import com.kira.ui.editorkit.widget.TextProcessor

class DirtyTextPlugin : EditorPlugin(PLUGIN_ID) {

    var onChangeListener: OnChangeListener? = null

    private var isDirty = false

    override fun onAttached(editText: TextProcessor) {
        super.onAttached(editText)
        Log.d(PLUGIN_ID, "DirtyText plugin loaded successfully!")
    }

    override fun onDetached(editText: TextProcessor) {
        super.onDetached(editText)
        onChangeListener = null
    }

    override fun afterTextChanged(text: Editable?) {
        super.afterTextChanged(text)
        if (!isDirty) {
            onChangeListener?.onContentChanged()
        }
    }

    override fun setTextContent(text: CharSequence) {
        super.setTextContent(text)
        isDirty = false
    }

    companion object {
        const val PLUGIN_ID = "dirty-text-9124"
    }
}