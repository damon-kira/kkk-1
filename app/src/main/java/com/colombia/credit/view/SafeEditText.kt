package com.colombia.credit.view

import android.content.Context
import android.text.Editable
import android.text.Selection
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.util.lib.log.debug
import kotlin.math.min

class SafeEditText : AppCompatEditText {

    private var mRealText: String? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun setSelection(start: Int, stop: Int) {
        val spannableText = text
        if (spannableText.isEmpty())
            return
        try {
            Selection.setSelection(spannableText, start, stop)
        } catch (e: IndexOutOfBoundsException) {
            debug {
                throw e
            }
        }
    }

    override fun setSelection(index: Int) {
        val spannableText = text
        if (spannableText.isNullOrEmpty())
            return
        try {
            Selection.setSelection(spannableText, index)
        } catch (e: IndexOutOfBoundsException) {
            debug {
                throw e
            }
        }
    }

    fun setText(text: String?, hideText: String?) {
        mRealText = text
        setText(hideText)
    }

    override fun getText(): Editable {
        return super.getText() ?: SpannableStringBuilder("")
    }

    fun getRealText(): String {
        val text = super.getText()?.toString()?.replace("-", "")
        if (text.isNullOrEmpty()) {
            return ""
        }
        val charArray = text.toCharArray()
        val realText = mRealText
        if (text.contains("*") && realText?.isNotEmpty() == true) {
            val len = min(text.length, realText.length)
            for (index in 0 until len) {
                if (text[index] == '*') {
                    charArray[index] = realText[index]
                }
            }
        }
        return String(charArray)
    }

    override fun isSuggestionsEnabled(): Boolean = false
}