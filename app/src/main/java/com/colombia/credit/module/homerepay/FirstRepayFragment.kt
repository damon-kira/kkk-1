package com.colombia.credit.module.homerepay

import android.view.View
import com.colombia.credit.databinding.FragmentHomeRepayBinding
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstRepayFragment: BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentHomeRepayBinding::inflate)

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        stopRefresh()
    }
}