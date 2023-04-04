package com.colombia.credit.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutToolbarBinding
import com.common.lib.expand.setBlockingOnClickListener
import com.util.lib.ifShow

/**
 * Created by weisl on 2019/10/22.
 */
class ToolbarLayout : RelativeLayout {

    private val GRAVITY_LEFT = 1
    private val GRAVITY_CENTER = 2
    private val GRAVITY_RIGHT = 3

    private val BACK_VISIBLE = 1
    private val BACK_GONE = 2

    private var mBinding: LayoutToolbarBinding =
        LayoutToolbarBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context) : this(context, null)
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

    fun init(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.ToolbarLayout).let { ta ->
            val indexCount = ta.indexCount
            for (index in 0 until indexCount) {
                val attr = ta.getIndex(index)
                when (attr) {
                    R.styleable.ToolbarLayout_tl_title -> {
                        val text = ta.getString(attr).orEmpty()
                        setText(text)
                    }
                    R.styleable.ToolbarLayout_tl_back_text_color -> {
                        val color = ta.getColor(attr, 0)
                        mBinding.toolbarTvTitle.setTextColor(color)
                    }
                    R.styleable.ToolbarLayout_tl_text_gravity -> {
                        val textGravity = ta.getInt(attr, GRAVITY_LEFT)
                        when (textGravity) {
                            GRAVITY_LEFT -> titleGravity(false)
                            GRAVITY_CENTER -> titleGravity(true)
                        }
                    }
                    R.styleable.ToolbarLayout_tl_back_visibility -> {
                        val backVisible = ta.getInt(attr, BACK_VISIBLE)
                        mBinding.toolbarAivBack.ifShow(backVisible == BACK_VISIBLE)
                    }
                    R.styleable.ToolbarLayout_tl_right_icon -> {
                        val rightImageRes = ta.getResourceId(attr, 0)
                        if (rightImageRes != 0) {
                            setRightImage(rightImageRes)
                        }
                    }
                    R.styleable.ToolbarLayout_tl_right_icon_show -> {
                        val isRightIconShow = ta.getBoolean(attr, false)
                        mBinding.toolbarAivRight.ifShow(isRightIconShow)
                    }
                    R.styleable.ToolbarLayout_tl_right_point_bg -> {
                        val drawable = ta.getDrawable(attr)
                        mBinding.toolbarAivPoint.background = drawable
                    }
                    R.styleable.ToolbarLayout_tl_left_icon-> {
                        val drawable = ta.getDrawable(attr)
                        mBinding.toolbarAivBack.setImageDrawable(drawable)
                    }
                    R.styleable.ToolbarLayout_tl_show_custom_icon -> {
                        val showCustom = ta.getBoolean(attr, false)
                        showCustomIcon(showCustom)
                    }
                }
            }
            ta.recycle()
        }
    }

    private fun titleGravity(isCenter: Boolean) {
        if (isCenter) {
            (mBinding.toolbarTvTitle.layoutParams as? LayoutParams)?.let {
                it.removeRule(RIGHT_OF)
                it.addRule(CENTER_HORIZONTAL)
            }
        } else {
            (mBinding.toolbarTvTitle.layoutParams as? LayoutParams)?.let {
                it.removeRule(CENTER_HORIZONTAL)
                it.addRule(RIGHT_OF, R.id.toolbar_aiv_back)
            }
        }
    }

    fun setText(text: String) {
        mBinding.toolbarTvTitle.text = text
    }

    fun setText(@StringRes text: Int) {
        mBinding.toolbarTvTitle.setText(text)
    }

    fun getTitleText(): String? = mBinding.toolbarTvTitle.text.toString()

    fun setOnbackListener(onClick: () -> Unit) {
        mBinding.toolbarAivBack.setBlockingOnClickListener {
            onClick()
        }
    }

    fun setTextColor(color: Int) {
        mBinding.toolbarTvTitle.setTextColor(color)
    }

    fun setRightImage(@DrawableRes rightImage: Int) {
        mBinding.toolbarAivRight.setImageResource(rightImage)
    }

    fun setRightImage(drawable: Drawable?) {
        mBinding.toolbarAivRight.setImageDrawable(drawable)
    }

    fun setBackImage(@DrawableRes leftImage: Int) {
        mBinding.toolbarAivBack.setImageResource(leftImage)
    }

    fun setBackImage(drawable: Drawable?) {
        mBinding.toolbarAivBack.setImageDrawable(drawable)
    }

    fun showNotifyPoint(isShow: Boolean) {
        if (mBinding.toolbarAivRight.visibility == View.VISIBLE) {
            mBinding.toolbarAivRight.ifShow(isShow)
        }
    }

    fun setNotifyListener(listener: () -> Unit) {
        mBinding.toolbarAivRight.setBlockingOnClickListener {
            listener()
        }
    }

    fun setPointBackground(drawable: Drawable?) {
        mBinding.toolbarAivPoint?.background = drawable
    }

    fun setCustomImage(@DrawableRes customImage: Int) {
        mBinding.toolbarMineCustom.setImageResource(customImage)
    }

    fun showCustomIcon(isShow: Boolean = false) {
        mBinding.toolbarMineCustom.ifShow(isShow)
//        if (mBinding.toolbarAivRight?.visibility == View.GONE) {
//            (mBinding.toolbarMineCustom?.layoutParams as? RelativeLayout.LayoutParams)?.let {
//                it.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                it.rightMargin = 16f.dp()
//            }
//        } else {
//            (mBinding.toolbarMineCustom?.layoutParams as? RelativeLayout.LayoutParams)?.let {
//                it.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                it.rightMargin = 54f.dp()
//            }
//        }
    }

    fun setCustomClickListener(listener: () -> Unit) {
        mBinding.toolbarMineCustom.setBlockingOnClickListener {
            listener()
        }
    }
}