package com.steve28.stedit

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class NewSteditField : AppCompatEditText {

    // 高亮器接口
    interface Highlighter {
        fun highlight(text: CharSequence): CharSequence
    }

    var highlighter: Highlighter? = null
        set(value) {
            field = value
            applyHighlighting()
        }

    // 文本内容（避免递归）
    var textContent: String
        get() = super.getText()?.toString() ?: ""
        set(value) {
            super.setText(value)
            applyHighlighting()
        }

    // 自定义文本监听器
    private val customTextWatchers = mutableListOf<TextWatcher>()
    private var isHighlighting = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        // 设置背景颜色
        setBackgroundColor(Color.parseColor("#FF002240"))

        // 添加内置的文本变化监听器
        super.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (!isHighlighting) {
                    customTextWatchers.forEach { it.beforeTextChanged(s, start, count, after) }
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isHighlighting) {
                    customTextWatchers.forEach { it.onTextChanged(s, start, before, count) }

                    // 应用高亮效果
                    applyHighlighting()
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (!isHighlighting) {
                    customTextWatchers.forEach { it.afterTextChanged(s) }
                }
            }
        })
    }

    // 应用高亮效果
    private fun applyHighlighting() {
        highlighter?.let { hl ->
            isHighlighting = true

            val originalText = text.toString()
            val highlightedText = hl.highlight(originalText)

            // 只更新文本并保持光标位置
            val selectionStart = selectionStart
            val selectionEnd = selectionEnd

            super.setText(highlightedText, BufferType.SPANNABLE)

            if (selectionStart >= 0 && selectionEnd >= 0) {
                setSelection(selectionStart, selectionEnd)
            }

            isHighlighting = false
        }
    }

    // 添加自定义文本监听器
    override fun addTextChangedListener(watcher: TextWatcher?) {
        watcher?.let {
            customTextWatchers.add(it)
        }
    }

    // 移除自定义文本监听器
    override fun removeTextChangedListener(watcher: TextWatcher?) {
        watcher?.let {
            customTextWatchers.remove(it)
        }
    }
}