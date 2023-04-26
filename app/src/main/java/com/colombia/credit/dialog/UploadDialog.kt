package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogUploadBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.viewbinding.binding

class UploadDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogUploadBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.88f, WRAP)
        setCancelable(false)
        setCanceledOnTouchOutside(true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.uploadView.start()
    }

    override fun dismiss() {
        super.dismiss()
        mBinding.uploadView.cancel()
    }

    fun resume(){
        mBinding.uploadView.animResume()
    }

    fun end(){
        mBinding.uploadView.end()
    }
}