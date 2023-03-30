package com.colombia.credit.dialog

import android.content.Context
import android.view.View
import com.colombia.credit.databinding.DialogKycSelectorBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class KycSelectorDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogKycSelectorBinding>()

    init {
        setContentView(mBinding.root)

        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    fun setOnClickListener(cameraListener: () -> Unit, albumListener: () -> Unit) {
        mBinding.tvCamera.setBlockingOnClickListener{
            cameraListener()
            dismiss()
        }
        mBinding.tvAlbum.setBlockingOnClickListener{
            albumListener()
            dismiss()
        }
    }
}