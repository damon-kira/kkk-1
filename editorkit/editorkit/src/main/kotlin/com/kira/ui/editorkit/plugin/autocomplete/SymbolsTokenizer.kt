

package com.kira.ui.editorkit.plugin.autocomplete

import android.widget.MultiAutoCompleteTextView

class SymbolsTokenizer : MultiAutoCompleteTextView.Tokenizer {

    companion object {
        private const val TOKEN = "!@#$%^&*()_+-={}|[]:;'<>/<.? \r\n\t"
    }

    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        var i = cursor
        while (i > 0 && !TOKEN.contains(text[i - 1])) {
            i--
        }
        while (i < cursor && text[i] == ' ') {
            i++
        }
        return i
    }

    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        var i = cursor
        while (i < text.length) {
            if (TOKEN.contains(text[i - 1])) {
                return i
            } else {
                i++
            }
        }
        return text.length
    }

    override fun terminateToken(text: CharSequence): CharSequence {
        return text
    }
}