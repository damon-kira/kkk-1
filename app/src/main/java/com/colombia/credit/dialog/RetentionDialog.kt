package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogRetentionBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class RetentionDialog(context: Context): DefaultDialog(context) {

    private val mBinding by binding<DialogRetentionBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.8f, WRAP)
    }

    fun setOnClickListener(listener: () -> Unit){
        mBinding.tvConfirm.setBlockingOnClickListener {
            listener()
            dismiss()
        }
    }
}