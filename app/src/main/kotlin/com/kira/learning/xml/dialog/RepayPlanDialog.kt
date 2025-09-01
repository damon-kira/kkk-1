package com.kira.learning.xml.dialog

import android.content.Context
import com.kira.learning.R
import com.kira.learning.model.res.RepayDetailInfo
import com.kira.learning.databinding.DialogRepayPlanBinding
import com.kira.learning.xml.expand.formatCommon
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class RepayPlanDialog constructor(context: Context): DefaultDialog(context) {

    private val mBinding by binding<DialogRepayPlanBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, WRAP, true)
        mBinding.aivClose.setBlockingOnClickListener { dismiss() }
    }

    fun setRepayData(data: RepayDetailInfo){
        mBinding.tvStageValue.text = data.stage_num
        mBinding.tvInterestValue.text = getString(R.string.amount_unit, formatCommon(data.stage_amount.orEmpty()))
        mBinding.tvTotalAmount.text = data.total_amount
        mBinding.tvRepaydataValue.text = data.repay_data
    }
}