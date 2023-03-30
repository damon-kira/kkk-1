package com.colombia.credit.module.process.bank

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivityBankInfoBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.viewbinding.binding

class BankInfoActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityBankInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        init()
    }

    private fun init() {
        mBinding.bankRbAhorrs.buttonDrawable = StateListDrawable()
        mBinding.bankRbCorriente.buttonDrawable = StateListDrawable()
        mBinding.bankRadiogroup.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.bank_rb_ahorrs -> {

                }
                R.id.bank_rb_corriente -> {

                }
            }
        }
        mBinding.bankRbAhorrs.isChecked = true
    }
}