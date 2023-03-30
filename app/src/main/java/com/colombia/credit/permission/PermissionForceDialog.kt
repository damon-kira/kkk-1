package com.colombia.credit.permission

import android.content.Context
import com.colombia.credit.databinding.DialogPermissionBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class PermissionForceDialog constructor(context: Context):DefaultDialog(context) {

    private val mBinding by binding<DialogPermissionBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.9f, WRAP)
        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    fun setOnClickListener(listener: () -> Unit): PermissionForceDialog {
        mBinding.tvSkip.setBlockingOnClickListener {
            listener.invoke()
            dismiss()
        }
        return this
    }
}