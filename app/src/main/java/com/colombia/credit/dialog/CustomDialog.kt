package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.app.getAppContext
import com.colombia.credit.databinding.DialogCutomBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.Launch
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.copyFile
import com.util.lib.ifShow

class CustomDialog constructor(context: Context) : DefaultDialog(context) {

    private val binding by binding<DialogCutomBinding>()

    init {
        setContentView(binding.root)
        setDisplaySize(0.85f, WRAP)
        setCancelable(true)
        setCanceledOnTouchOutside(false)

        binding.etvWhatsapp.ifShow(!getWhatsAppTel().isNullOrEmpty())
        binding.etvTelephone.ifShow(!getServiceTel().isNullOrEmpty())
        binding.etvEmail.ifShow(!getEmail().isNullOrEmpty())

        binding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
        binding.etvWhatsapp.setBlockingOnClickListener {
            Launch.skipWhatsApp(getAppContext())
            dismiss()
        }
        binding.etvEmail.setBlockingOnClickListener{
            val email = getEmail()
            copyClick(email)
            toast(R.string.copy_success)
            dismiss()
        }
        binding.etvTelephone.setBlockingOnClickListener {
            Launch.skipCallPage(getAppContext())
            dismiss()
        }
    }
}