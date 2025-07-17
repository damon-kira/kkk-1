

package com.kira.ui.editorkit.utils

import android.widget.TextView

val TextView.topVisibleLine: Int
    get() {
        if (layout == null || lineHeight == 0) {
            return 0
        }
        val line = layout.getLineForVertical(scrollY)
        if (line < 0) {
            return 0
        }
        return if (line >= lineCount) {
            lineCount - 1
        } else {
            line
        }
    }

val TextView.bottomVisibleLine: Int
    get() {
        if (layout == null || lineHeight == 0) {
            return 0
        }
        val line = layout.getLineForVertical(scrollY + height)
        if (line < 0) {
            return 0
        }
        return if (line >= lineCount) {
            lineCount - 1
        } else {
            line
        }
    }