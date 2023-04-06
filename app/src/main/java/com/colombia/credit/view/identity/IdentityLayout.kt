package com.colombia.credit.view.identity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.colombia.credit.databinding.LayoutIdentityBinding
import com.common.lib.expand.setBlockingOnClickListener


class IdentityLayout : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val mBinding: LayoutIdentityBinding =
        LayoutIdentityBinding.inflate(LayoutInflater.from(context), this)

    fun setLeftImage(drawable: Drawable) {
        mBinding.ivLeft.setImageDrawable(drawable)
    }

    fun setLeftImage(bitmap: Bitmap) {
        mBinding.ivLeft.setImageBitmap(bitmap)
    }

    fun setLeftImage(@DrawableRes res: Int) {
        mBinding.ivLeft.setImageDrawable(res)
    }

    fun setRightImage(drawable: Drawable) {
        mBinding.ivRight.setImageDrawable(drawable)
    }

    fun setRightImage(bitmap: Bitmap) {
        mBinding.ivRight.setImageBitmap(bitmap)
    }

    fun setRightImage(@DrawableRes res: Int) {
        mBinding.ivRight.setImageDrawable(res)
    }

    fun setLeftStatus(@IdentityPicStatus status: Int) {
        mBinding.ivLeft.toggleStatus(status)
    }

    fun setRightStatus(@IdentityPicStatus status: Int) {
        mBinding.ivRight.toggleStatus(status)
    }

    fun setEnable(leftEnable: Boolean, rightEnable: Boolean) {
        mBinding.ivLeft.isEnabled  = leftEnable
        mBinding.ivRight.isEnabled = rightEnable
    }

    fun setClickListener(leftListener: (View) -> Unit, rightListener: (View) -> Unit) {
        mBinding.ivLeft.setBlockingOnClickListener(leftListener)
        mBinding.ivRight.setBlockingOnClickListener(rightListener)
    }
}