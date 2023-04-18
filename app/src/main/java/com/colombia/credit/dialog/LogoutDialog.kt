package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogLogoutConfirmBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class LogoutDialog constructor(context: Context) : DefaultDialog(context) {

    private val binding by binding<DialogLogoutConfirmBinding>()

    init {
        setContentView(binding.root)
        setCancelable(true)
        setDisplaySize(0.88f, WRAP)
        setCanceledOnTouchOutside(false)
        binding.tvCancel.setBlockingOnClickListener { dismiss() }
    }

    fun setLogoutListener(listener: () -> Unit): LogoutDialog {
        binding.tvLogout.setBlockingOnClickListener {
            listener.invoke()
            dismiss()
        }
        return this
    }
}