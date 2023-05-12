package com.colombia.credit.dialog

import android.content.Context
import android.os.Bundle
import com.colombia.credit.databinding.DialogCancelAutohintBinding
import com.colombia.credit.expand.getUnitString
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

// 取消自动确认额度弹窗
class CancelAutoHintDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogCancelAutohintBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDisplaySize(0.88f, WRAP)
        mBinding.etvCancel.setBlockingOnClickListener { dismiss() }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    fun setAmount(amount: String): CancelAutoHintDialog{
        mBinding.tvAmount.text = getUnitString(amount)
        return this
    }

    fun setConfirmListener(listener: () -> Unit): CancelAutoHintDialog{
        mBinding.tvConfirm.setBlockingOnClickListener {
            dismiss()
            listener.invoke()
        }
        return this
    }
}