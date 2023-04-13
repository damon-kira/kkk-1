package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.databinding.DialogProcessRetentionBinding
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.mFirstPageLoanAmount
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class ProcessBackDialog constructor(context: Context): DefaultDialog(context) {

    private val mBinding by binding<DialogProcessRetentionBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.86f,WRAP)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        mBinding.retentionTvCancel.setBlockingOnClickListener {
            dismiss()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.tvAmount.text = getUnitString(mFirstPageLoanAmount)
    }

    fun setOnClickListener(clickListener: () -> Unit): ProcessBackDialog {
        mBinding.retentionTvBack.setBlockingOnClickListener {
            dismiss()
            clickListener.invoke()
        }
        return this
    }

}