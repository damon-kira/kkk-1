package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogNetErrorBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class NetErrorDialog(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogNetErrorBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.88f, WRAP)
        mBinding.netAivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    fun setonClickListener(refresh: () -> Unit, mobileNet: () -> Unit, wifi: () -> Unit) {
        mBinding.netTvRefresh.setBlockingOnClickListener {
            refresh.invoke()
            dismiss()
        }
        mBinding.netTvMobileNet.setBlockingOnClickListener {
            mobileNet.invoke()
            dismiss()
        }
        mBinding.netTvWifi.setBlockingOnClickListener {
            wifi.invoke()
            dismiss()
        }
    }
}