package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.databinding.DialogRepayDetailBinding
import com.colombia.credit.expand.formatCommon
import com.common.lib.dialog.DefaultDialog
import com.common.lib.viewbinding.binding

// 还款详情
class RepayDetailDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogRepayDetailBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.85f, WRAP)
    }

    fun setDetail(
        loan: String,
        interest: String,
        overdue: String,
        repay: String
    ): RepayDetailDialog {
        mBinding.iilLoan.setRightText(getUnitText(loan))
        mBinding.iilInterest.setRightText(getUnitText(interest))
        mBinding.iilOverdue.setRightText(getUnitText(overdue))
        mBinding.iilRepay.setRightText(getUnitText(repay))
        return this
    }

    private fun getUnitText(loan: String) = getString(R.string.amount_unit, formatCommon(loan))
}