package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogFirstLoanBinding
import com.colombia.credit.expand.isNewUser
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class FirstLoanHintDialog constructor(context: Context):DefaultDialog(context) {

    private val mBinding by binding<DialogFirstLoanBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setDisplaySize(0.85f, WRAP)
    }

    fun setOnClickListener(listener: () -> Unit): FirstLoanHintDialog{
        mBinding.retentionTvBtn.setBlockingOnClickListener {
            listener.invoke()
            dismiss()
        }
        return this
    }

    override fun show() {
        super.show()
        isNewUser = false
    }
}