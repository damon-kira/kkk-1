package com.colombia.credit.module.refused

import android.view.View
import com.colombia.credit.databinding.FragmentRefusedBinding
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

//拒绝页面
@AndroidEntryPoint
class RefusedFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentRefusedBinding::inflate)

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        stopRefresh()
    }
}