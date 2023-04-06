package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.app.getAppContext
import com.colombia.credit.databinding.DialogCutomBinding
import com.colombia.credit.manager.Launch
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class CustomDialog constructor(context: Context) : DefaultDialog(context) {

    private val binding by binding<DialogCutomBinding>()

    init {
        setContentView(binding.root)
        setDisplaySize(0.85f, WRAP)
        setCancelable(true)
        setCanceledOnTouchOutside(false)

        binding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    fun setCustomClickListener(): CustomDialog {
        binding.etvWhatsapp.setBlockingOnClickListener {
            Launch.skipWhatsApp(getAppContext())
            dismiss()
        }
        binding.etvEmail.setBlockingOnClickListener{
            Launch.skipToEmail(getAppContext())
            dismiss()
        }
        binding.etvTelephone.setBlockingOnClickListener {
            Launch.skipCallPage(getAppContext())
            dismiss()
        }
        return this
    }
}