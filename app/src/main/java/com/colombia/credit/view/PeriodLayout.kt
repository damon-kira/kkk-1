package com.colombia.credit.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutPeriodViewBinding
import com.util.lib.ifShow

class PeriodLayout : ConstraintLayout {
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

    private val mBinding = LayoutPeriodViewBinding.inflate(LayoutInflater.from(context), this)

    private fun init(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.PeriodLayout).let { ta ->
            val indexCount = ta.indexCount
            for (index in 0 until indexCount) {
                val attr = ta.getIndex(index)
                when (attr) {
                    R.styleable.PeriodLayout_pl_text -> {
                        val text = ta.getString(attr)
                        setPeriod(text)
                    }
                    R.styleable.PeriodLayout_pl_lock_visiblity -> {
                        val isShow = ta.getBoolean(attr, false)
                        showLock(isShow)
                    }
                }
            }
            ta.recycle()
        }
    }

    private fun changeTextColor(isShowLock: Boolean) {
        if (isShowLock) {
            mBinding.tvPeriod.setTextColor(ContextCompat.getColor(context, R.color.color_666666))
            mBinding.tvPeriodText.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_999999
                )
            )
        } else {
            mBinding.tvPeriod.setTextColor(ContextCompat.getColor(context, R.color.white))
            mBinding.tvPeriodText.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    fun setPeriod(period: String?) {
        mBinding.tvPeriod.text = period
    }

    fun showLock(isShow: Boolean) {
        mBinding.aivLock.ifShow(isShow)
        changeTextColor(isShow)
    }

}