package com.kira.learning.module.defer

import com.kira.learning.R
import com.kira.learning.bean.res.RspRepayOrders
import com.kira.learning.expand.getUnitString
import com.kira.learning.manager.Launch
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