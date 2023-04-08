package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogProcessRetentionBinding
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.expand.mFirstPageLoanAmount
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class ProcessBackDialog constructor(context: Context): DefaultDialog(context) {

    private val mBinding by binding<DialogProcessRetentionBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mBinding.retentionTvCancel.setBlockingOnClickListener {
            dismiss()
        }
        mBinding.tvAmount.text = formatCommon(mFirstPageLoanAmount.toString())
    }

    fun setOnClickListener(clickListener: () -> Unit): ProcessBackDialog {
        mBinding.retentionTvBack.setBlockingOnClickListener {
            clickListener.invoke()
            dismiss()
        }
        return this
    }

}