package com.kira.learning.xml.dialog

import android.content.Context
import com.kira.learning.databinding.DialogRetentionBinding
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