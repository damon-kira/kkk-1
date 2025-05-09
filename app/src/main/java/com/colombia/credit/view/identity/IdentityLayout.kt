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


class IdentityLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

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

    fun isAllSuccess(): Boolean {
        return mBinding.ivLeft.getStatus() == IdentityPicStatus.STATUS_SUCCESS && mBinding.ivRight.getStatus() == IdentityPicStatus.STATUS_SUCCESS
    }

    fun isLeftSuccess() = mBinding.ivLeft.getStatus() == IdentityPicStatus.STATUS_SUCCESS

    fun isRightSuccess() = mBinding.ivRight.getStatus() == IdentityPicStatus.STATUS_SUCCESS

    fun setEnable(leftEnable: Boolean, rightEnable: Boolean) {
        setLeftEnable(leftEnable)
        setRightEnable(rightEnable)
    }

    fun setLeftEnable(leftEnable: Boolean) {
        mBinding.ivLeft.isEnabled = leftEnable
    }

    fun setRightEnable(leftEnable: Boolean) {
        mBinding.ivRight.isEnabled = leftEnable
    }

    fun setClickListener(leftListener: (View) -> Unit, rightListener: (View) -> Unit) {
        mBinding.ivLeft.setBlockingOnClickListener(leftListener)
        mBinding.ivRight.setBlockingOnClickListener(rightListener)
    }
}