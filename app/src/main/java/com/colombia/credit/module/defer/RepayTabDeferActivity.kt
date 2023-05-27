package com.colombia.credit.module.defer

import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspRepayOrders
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.manager.Launch
import com.util.lib.GsonUtil
import dagger.hilt.android.AndroidEntryPoint

// 展期
@AndroidEntryPoint
open class RepayTabDeferActivity : DeferActivity() {

    override fun getInfo() {
        intent.getStringExtra(EXTRA_INFO)?.let {
            val detail = GsonUtil.fromJsonNew<RspRepayOrders.RepayOrderDetail>(it)
            jine = detail?.X32HrYq4u.orEmpty()
            val amount = getUnitString(jine)
            mBinding.tvAmount.text = amount
            mBinding.iilDays.setRightText(getString(R.string.days, detail?.GHMXDjtsUn ?: "--"))
            mBinding.iilDate.setRightText(detail?.prr9Ie61.orEmpty())
            mBinding.iilAmount.setRightText(getUnitString(detail?.rCC18KSG.orEmpty()))
            mBinding.tvApply.text = getString(R.string.repay_amount_value, amount)
            orderId = detail?.W5KW6.orEmpty()
            mLoanId = detail?.X32HrYq4u.orEmpty()
        }
    }

    override fun checkOrderRepay() {
        Launch.skipMainActivity(this)
        finish()
    }
}