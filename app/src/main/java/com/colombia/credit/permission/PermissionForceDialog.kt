package com.colombia.credit.permission

import android.content.Context
import com.colombia.credit.databinding.DialogPermissionBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class PermissionForceDialog constructor(context: Context):DefaultDialog(context) {

    private val mBinding by binding<DialogPermissionBinding>()

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

    fun setMessage(message: String): PermissionForceDialog {
        mBinding.tvPermissionText.text = message
        return this
    }

    fun setOnCloseListener(listener: () -> Unit):PermissionForceDialog{
        mCloseListener = listener
        return this
    }

    fun setOnClickListener(listener: () -> Unit): PermissionForceDialog {
        mBinding.tvSkip.setBlockingOnClickListener {
            listener.invoke()
            dismiss()
        }
        return this
    }
}