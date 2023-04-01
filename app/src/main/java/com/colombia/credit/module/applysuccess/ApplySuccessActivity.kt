package com.colombia.credit.module.applysuccess

import android.os.Bundle
import com.colombia.credit.databinding.ActivityApplySuccessBinding
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApplySuccessActivity: BaseActivity() {

    private val mBinding by binding<ActivityApplySuccessBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}