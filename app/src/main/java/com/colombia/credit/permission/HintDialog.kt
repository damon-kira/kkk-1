package com.colombia.credit.permission

import android.content.Context
import androidx.annotation.DrawableRes
import com.colombia.credit.databinding.DialogHintBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.ifShow

class HintDialog constructor(context: Context) : DefaultDialog(context) {

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

    fun setTitleText(title: String): HintDialog {
        mBinding.tvTitle.text = title
        return this
    }

    fun setIcon(@DrawableRes resId: Int): HintDialog {
        mBinding.aivImage.setImageResource(resId)
        return this
    }

    fun setMessage(message: String): HintDialog {
        mBinding.tvPermissionText.text = message
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