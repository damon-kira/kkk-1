

package com.kira.ui.editorkit.model

import android.text.TextPaint
import android.text.style.CharacterStyle

class SyntaxHighlightSpan(private val span: StyleSpan) : CharacterStyle() {

    override fun updateDrawState(textPaint: TextPaint?) {
        textPaint?.color = span.color
        textPaint?.isFakeBoldText = span.bold
        textPaint?.isUnderlineText = span.underline
        if (span.italic) {
            textPaint?.textSkewX = -0.1f
        }
        if (span.strikethrough) {
            textPaint?.flags = TextPaint.STRIKE_THRU_TEXT_FLAG
        }
    }
}