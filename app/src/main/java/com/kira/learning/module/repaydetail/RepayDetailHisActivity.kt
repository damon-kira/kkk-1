package com.kira.learning.module.repaydetail

import com.kira.learning.manager.Launch
import dagger.hilt.android.AndroidEntryPoint

// 历史订单进入
@AndroidEntryPoint
class RepayDetailHisActivity : RepayDetailActivity() {

    override fun checkOrderResult() {
        Launch.skipHistoryActivity(this)
        finish()
    }

}