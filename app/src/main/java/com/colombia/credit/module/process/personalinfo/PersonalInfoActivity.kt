package com.colombia.credit.module.process.personalinfo

import android.os.Bundle
import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoActivity: BaseProcessActivity() {

    private val mBinding by binding<ActivityPersonalInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}