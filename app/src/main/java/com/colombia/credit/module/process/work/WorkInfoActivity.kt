package com.colombia.credit.module.process.work

import android.os.Bundle
import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.databinding.ActivityWorkInfoBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkInfoActivity: BaseProcessActivity() {

    private val mBinding by binding<ActivityWorkInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}