package com.colombia.credit.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutItemInfoBinding
import com.util.lib.dp
import com.util.lib.ifShow
import com.util.lib.isHide
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
    ) {
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
                        mBinding.tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize.toFloat())
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
                        mBinding.tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize.toFloat())
                    }
                    R.styleable.ItemInfoLayout_iil_right_text_color -> {
                        val color = ta.getColor(attr, LEFT_NORMAL_COLOR)
                        mBinding.tvValue.setTextColor(color)
                    }
                    R.styleable.ItemInfoLayout_iil_line_show -> {
                        val isShow = ta.getBoolean(attr, true)
                        mBinding.line.ifShow(isShow)
                    }
                    R.styleable.ItemInfoLayout_iil_right_image -> {
                        val drawableRes = ta.getResourceId(attr, 0)
                        if (drawableRes != 0) {
                            setRightImage(drawableRes)
                        }
                    }
                    R.styleable.ItemInfoLayout_iil_right_image_width -> {
                        val drawWidth = ta.getDimensionPixelSize(attr, 0)
                        if (drawWidth == 0) return
                        mBinding.aivRight.updateLayoutParams<LayoutParams> {
                            width = drawWidth
                        }
                    }
                    R.styleable.ItemInfoLayout_iil_right_image_height -> {
                        val drawHeight = ta.getDimensionPixelSize(attr, 0)
                        if (drawHeight == 0) return
                        mBinding.aivRight.updateLayoutParams<LayoutParams> {
                            height = drawHeight
                        }
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

    fun setRightImage(@DrawableRes drawRes: Int) {
        mBinding.aivRight.ifShow(drawRes != 0)
        mBinding.aivRight.setImageResource(drawRes)
        updateDrawablePadding()
    }

    fun setRightImage(drawable: Drawable?) {
        mBinding.aivRight.ifShow(drawable != null)
        mBinding.aivRight.setImageDrawable(drawable)
        updateDrawablePadding()
    }

    private fun updateDrawablePadding() {
        mBinding.tvValue.updateLayoutParams<LayoutParams> {
            marginEnd = if (mBinding.aivRight.isHide()) 0 else 4f.dp()
        }
    }
}