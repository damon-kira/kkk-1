package com.colombia.credit.view.identity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutIdentityViewBinding
import com.common.lib.expand.setBlockingOnClickListener

class IdentityView : ConstraintLayout {

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

    private val mBinding: LayoutIdentityViewBinding = LayoutIdentityViewBinding.inflate(
        LayoutInflater.from(context), this
    )

    private fun init(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.IdentityView).let { ta ->
            val indexCount = ta.indexCount
            for (index in 0 until indexCount) {
                when (val attr = ta.getIndex(index)) {
                    R.styleable.IdentityView_iv_text -> {
                        val text = ta.getString(attr)
                        setText(text)
                    }
                    R.styleable.IdentityView_iv_image -> {
                        val resId = ta.getResourceId(attr, -1)
                        if (resId != -1) {
                            AppCompatResources.getDrawable(context, resId)?.let { drawable ->
                                setImageDrawable(drawable)
                            }
                        }
                    }
                }
            }
            ta.recycle()
        }
    }

    fun setOnClickListener(listener: (View) -> Unit) {
        mBinding.identityAiv.setBlockingOnClickListener(listener)
    }

    fun setImageDrawable(drawable: Drawable) {
        mBinding.identityAiv.setImageDrawable(drawable)
    }

    fun setImageDrawable(@DrawableRes drawableRes: Int) {
        mBinding.identityAiv.setImageResource(drawableRes)
    }

    fun setImageBitmap(bitmap: Bitmap) {
        mBinding.identityAiv.setImageBitmap(bitmap)
    }

    fun setText(text: String?) {
        mBinding.identityTv.text = text
    }

    fun setText(@StringRes textRes: Int) {
        mBinding.identityTv.setText(textRes)
    }

    fun getImageView() = mBinding.identityAiv

    fun toggleStatus(@IdentityPicStatus status: Int) {
        when (status) {
            IdentityPicStatus.STATUS_ERROR -> mBinding.aivStatus.setImageResource(R.drawable.svg_error_hint_24)
            IdentityPicStatus.STATUS_SUCCESS -> mBinding.aivStatus.setImageResource(R.drawable.ic_kyc_success)
            else -> {
                mBinding.aivStatus.setImageResource(R.drawable.ic_kyc_camera)
            }
        }
    }
}