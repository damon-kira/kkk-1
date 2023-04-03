package com.colombia.credit.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutMeItemBinding

class AccountItemLayout : ConstraintLayout {
    constructor(context: Context) : super(context)
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

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private val binding = LayoutMeItemBinding.inflate(LayoutInflater.from(context), this)

    private fun init(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.AccountItemLayout).let { ta ->
            val indexCount = ta.indexCount
            for (index in 0 until indexCount) {
                when (val attr = ta.getIndex(index)) {
                    R.styleable.AccountItemLayout_ail_text -> {
                        val text = ta.getString(attr)
                        setText(text)
                    }
                    R.styleable.AccountItemLayout_ail_left_icon -> {
                        val drawable = ta.getDrawable(attr)
                        setLeftIcon(drawable)
                    }
                }
            }
            ta.recycle()
        }
    }

    fun setText(text: String?) {
        binding.tvText.text = text
    }

    fun setText(@StringRes textRes: Int) {
        binding.tvText.setText(textRes)
    }

    fun setLeftIcon(@DrawableRes drawableRes: Int) {
        binding.aivIcon.setImageResource(drawableRes)
    }

    fun setLeftIcon(drawable: Drawable?) {
        binding.aivIcon.setImageDrawable(drawable)
    }
}