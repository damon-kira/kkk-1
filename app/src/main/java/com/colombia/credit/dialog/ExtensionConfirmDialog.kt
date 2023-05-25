package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.databinding.DialogHintBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.dp

// 展期确认弹窗
class ExtensionConfirmDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogHintBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setDisplaySize(0.88f, WRAP)
        mBinding.tvTitle.setText(R.string.defer_confirm1)
        mBinding.tvText.setText(R.string.defer_confirm2)
        mBinding.tvSkip.setText(R.string.defer_apply)
        val padding = 25f.dp()
        mBinding.tvSkip.setPadding(
            padding,
            mBinding.tvSkip.paddingTop,
            padding,
            mBinding.tvSkip.paddingBottom
        )
        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    fun setOnClickListener(confirm: () -> Unit): ExtensionConfirmDialog {
        mBinding.tvSkip.setBlockingOnClickListener {
            dismiss()
            confirm.invoke()
        }
        return this
    }
}