package com.colombia.credit.permission

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DrawableRes
import com.colombia.credit.databinding.DialogHintBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.ifShow
import com.util.lib.invisible

class HintDialog constructor(context: Context) : DefaultDialog(context) {

    companion object {
        const val TYPE_VISIBLE = 0x10
        const val type_GONE = 0x11
        const val TYPE_INVISIBLE = 0x12
    }

    private val mBinding by binding<DialogHintBinding>()

    private var mCloseListener: (() -> Unit)? = null

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.9f, WRAP)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mBinding.aivClose.setBlockingOnClickListener {
            mCloseListener?.invoke()
            dismiss()
        }
    }

    fun showClose(ifShow: Boolean): HintDialog {
        mBinding.aivClose.ifShow(ifShow)
        return this
    }

    fun showTitle(visibility: Int): HintDialog {
        when (visibility) {
            type_GONE ->mBinding.tvTitle.ifShow(false)
            TYPE_VISIBLE ->mBinding.tvTitle.ifShow(true)
            TYPE_INVISIBLE ->mBinding.tvTitle.invisible()
        }

        return this
    }

    fun showBtn(show: Boolean): HintDialog {
        mBinding.tvSkip.ifShow(show)
        return this
    }

    fun setTitleText(title: String): HintDialog {
        mBinding.tvTitle.text = title
        return this
    }

    fun setIcon(@DrawableRes resId: Int): HintDialog {
        mBinding.aivImage.setImageResource(resId)
        return this
    }

    fun setMessage(message: String): HintDialog {
        mBinding.tvText.text = message
        return this
    }

    fun setMessageTextSize(textSize: Float): HintDialog {
        mBinding.tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        return this
    }

    fun setBtnText(btnText: String): HintDialog {
        mBinding.tvSkip.text = btnText
        return this
    }

    fun updateBtnPadding(
        left: Int = mBinding.tvSkip.paddingLeft,
        top: Int = mBinding.tvSkip.paddingTop,
        right: Int = mBinding.tvSkip.paddingRight,
        bottom: Int = mBinding.tvSkip.paddingBottom
    ): HintDialog {
        mBinding.tvSkip.setPadding(left, top, right, bottom)
        return this
    }

    fun setOnCloseListener(listener: () -> Unit): HintDialog {
        mCloseListener = listener
        return this
    }

    fun setOnClickListener(listener: () -> Unit): HintDialog {
        mBinding.tvSkip.setBlockingOnClickListener {
            dismiss()
            listener.invoke()
        }
        return this
    }
}