package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogKycSelectorBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class PicImageDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogKycSelectorBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, WRAP, true)
        setCancelable(true )
        setCanceledOnTouchOutside(false)
        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    fun setOnImageClick(cameraListener: () -> Unit, albumListener: () -> Unit): PicImageDialog {
        mBinding.tvCamera.setBlockingOnClickListener{
            cameraListener()
            dismiss()
        }
        mBinding.tvAlbum.setBlockingOnClickListener{
            albumListener()
            dismiss()
        }
        return this
    }
}