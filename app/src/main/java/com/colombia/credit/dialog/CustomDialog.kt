package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.app.getAppContext
import com.colombia.credit.databinding.DialogCutomBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.service.SerManager
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.ifShow

class CustomDialog constructor(context: Context) : DefaultDialog(context) {

    private val binding by binding<DialogCutomBinding>()

    init {
        setContentView(binding.root)
        setDisplaySize(0.85f, WRAP)
        setCancelable(true)
        setCanceledOnTouchOutside(false)

        val whatsapp = getWhatsAppTel()
        val tel = getServiceTel()
        val email = getEmail()

        binding.etvWhatsapp.ifShow(whatsapp.isNotEmpty())
        binding.etvTelephone.ifShow(tel.isNotEmpty())
        binding.etvEmail.ifShow(email.isNotEmpty())

        if (whatsapp.isEmpty() && tel.isEmpty() && email.isEmpty()) {
            SerManager.getCustom()
        }

        binding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
        binding.etvWhatsapp.setBlockingOnClickListener {
            Launch.skipWhatsApp(getAppContext())
            dismiss()
        }
        binding.etvEmail.setBlockingOnClickListener {
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