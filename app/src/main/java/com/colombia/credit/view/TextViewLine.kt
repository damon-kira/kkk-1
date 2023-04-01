package com.colombia.credit.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.colombia.credit.R


class TextViewLine : AppCompatTextView {
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private var isShowLine: Boolean = true
    private var isCenter: Boolean = false

    private fun init(context: Context?, attrs: AttributeSet?) {
        attrs ?: return
        context?.obtainStyledAttributes(attrs, R.styleable.TextViewLine)?.let {
            if (it.hasValue(R.styleable.TextViewLine_tvl_center)) {
                isCenter = it.getBoolean(R.styleable.TextViewLine_tvl_center, false)
            }
            if (it.hasValue(R.styleable.TextViewLine_tvl_isShow)) {
                isShowLine = it.getBoolean(R.styleable.TextViewLine_tvl_isShow, false)
            }
            it.recycle()
        }
        showLine(isCenter)
    }

    fun showTextLine(isShow: Boolean) {
        isShowLine = isShow
        showLine(isCenter)
        postInvalidate()
    }

    private fun showLine(isCenter: Boolean) {
        if (isShowLine) {
            if (isCenter) {
                paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                paint.flags = Paint.UNDERLINE_TEXT_FLAG
            }
        }
        paint.isAntiAlias = true
    }
}