package com.colombia.credit.module.defer

import android.os.Bundle
import com.colombia.credit.bean.resp.RspRepayOrders
import com.colombia.credit.databinding.ActivityDeferBinding
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

// 展期
@AndroidEntryPoint
open class RepayTabDeferActivity : DeferActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val orderDetail = intent.getParcelableExtra<RspRepayOrders.RepayOrderDetail>(EXTRA_INFO)
//        orderDetail?.let {
//            mBinding.tvAmount
//        }
    }
}