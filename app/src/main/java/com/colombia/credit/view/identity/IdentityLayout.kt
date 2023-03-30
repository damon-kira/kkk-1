package com.colombia.credit.view.identity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.colombia.credit.databinding.LayoutIdentityBinding

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

    }

    fun setLeftImage(bitmap: Bitmap) {

    }

    fun setLeftImage(@DrawableRes res: Int) {

    }

    fun setRightImage(drawable: Drawable) {

    }

    fun setRightImage(bitmap: Bitmap) {

    }

    fun setRightImage(@DrawableRes res: Int) {
    }
}