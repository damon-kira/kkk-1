package com.colombia.credit.module.repaydetail

import android.os.Bundle
import com.colombia.credit.databinding.ActivityRepayDetailBinding
import com.colombia.credit.expand.showCustomDialog
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

// 首贷还款详情
@AndroidEntryPoint
class RepayDetailActivity: BaseActivity() {

    private val mBinding by binding<ActivityRepayDetailBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }

        mBinding.tvApply.setBlockingOnClickListener {
            Launch.skipWebViewActivity(this, H5UrlManager.URL_PAY)
        }
        mBinding.tvExtension.setBlockingOnClickListener {
            Launch.skipDeferActivity(this)
        }
    }
}