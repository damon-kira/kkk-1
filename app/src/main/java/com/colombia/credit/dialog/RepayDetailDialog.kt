package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogRepayDetailBinding
import com.colombia.credit.expand.getUnitString
import com.common.lib.dialog.DefaultDialog
import com.common.lib.viewbinding.binding

// 还款详情
class RepayDetailDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogRepayDetailBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.85f, WRAP)
    }

    private var text1: String? = null
    private var text2:String? = null
    private var text3: String? = null
    private var text4: String? = null

    fun setDetail(
        loan: String,
        interest: String,
        overdue: String,
        repay: String
    ): RepayDetailDialog {
        text1 = loan
        text2 = interest
        text3 = overdue
        text4 = repay
        return this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.iilLoan.setRightText(getUnitString(text1))
        mBinding.iilInterest.setRightText(getUnitString(text2))
        mBinding.iilOverdue.setRightText(getUnitString(text3))
        mBinding.iilRepay.setRightText(getUnitString(text4))
    }
}