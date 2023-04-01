package com.colombia.credit.module.defer

import android.os.Bundle
import com.colombia.credit.databinding.ActivityDeferBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

// 展期
@AndroidEntryPoint
class DeferActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityDeferBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.tvApply.setBlockingOnClickListener {
            // 调用支付
        }
    }
}