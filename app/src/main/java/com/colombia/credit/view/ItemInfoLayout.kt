package com.colombia.credit.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutItemInfoBinding
import com.util.lib.ifShow
import com.util.lib.sp

class ItemInfoLayout : ConstraintLayout {

    private val mBinding = LayoutItemInfoBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context, attrs)
    }

    private val NORMAL_TEXTSIZE = 12.sp().toInt()
    private val LEFT_NORMAL_COLOR = Color.parseColor("#666666")
    private val RIGHT_NORMAL_COLOR = Color.parseColor("#333333")

    private fun init(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.ItemInfoLayout).let { ta ->
            val indexCount = ta.indexCount
            for (index in 0 until indexCount) {
                when (val attr = ta.getIndex(index)) {
                    R.styleable.ItemInfoLayout_iil_left_text -> {
                        val text = ta.getString(attr).orEmpty()
                        setLeftText(text)
                    }
                    R.styleable.ItemInfoLayout_iil_left_text_bold -> {
                        val bold = ta.getBoolean(attr, false)
                        if (bold) {
                            mBinding.tvText.typeface = Typeface.DEFAULT_BOLD
                        }
                    }
                    R.styleable.ItemInfoLayout_iil_left_text_size -> {
                        val textsize = ta.getDimensionPixelSize(attr, NORMAL_TEXTSIZE)
                        mBinding.tvText.textSize = textsize.toFloat()
                    }
                    R.styleable.ItemInfoLayout_iil_left_text_color -> {
                        val color = ta.getColor(attr, LEFT_NORMAL_COLOR)
                        mBinding.tvText.setTextColor(color)
                    }

                    R.styleable.ItemInfoLayout_iil_right_text -> {
                        val text = ta.getString(attr).orEmpty()
                        setRightText(text)
                    }
                    R.styleable.ItemInfoLayout_iil_right_text_bold -> {
                        val bold = ta.getBoolean(attr, false)
                        if (bold) {
                            mBinding.tvValue.typeface = Typeface.DEFAULT_BOLD
                        }
                    }
                    R.styleable.ItemInfoLayout_iil_right_text_size -> {
                        val textsize = ta.getDimensionPixelSize(attr, RIGHT_NORMAL_COLOR)
                        mBinding.tvValue.textSize = textsize.toFloat()
                    }
                    R.styleable.ItemInfoLayout_iil_right_text_color -> {
                        val color = ta.getColor(attr, LEFT_NORMAL_COLOR)
                        mBinding.tvValue.setTextColor(color)
                    }
                    R.styleable.ItemInfoLayout_iil_line_show -> {
                        val isShow = ta.getBoolean(attr, true)
                        mBinding.line.ifShow(isShow)
                    }
                    else -> {}
                }
            }
            ta.recycle()
        }
    }

    fun setLeftText(text: String) {
        mBinding.tvText.text = text
    }

    fun setLeftText(@StringRes textRes: Int) {
        mBinding.tvText.setText(textRes)
    }

    fun setRightText(text: String) {
        mBinding.tvValue.text = text
    }

    fun setRightText(@StringRes textRes: Int) {
        mBinding.tvValue.setText(textRes)
    }
}