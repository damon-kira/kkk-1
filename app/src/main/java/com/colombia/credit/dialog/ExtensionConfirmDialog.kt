package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.databinding.DialogPermissionBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

// 展期确认弹窗
class ExtensionConfirmDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogPermissionBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        mBinding.tvTitle.setText(R.string.defer_confirm1)
        mBinding.tvPermissionText.setText(R.string.defer_confirm2)
        mBinding.tvSkip.setText(R.string.defer_apply)
    }

    fun setOnClickListener(confirm: () -> Unit): ExtensionConfirmDialog {
        mBinding.tvSkip.setBlockingOnClickListener {
            confirm.invoke()
        }
        return this
    }
}