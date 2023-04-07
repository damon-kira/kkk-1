package com.colombia.credit.module.home

import android.view.View
import com.colombia.credit.databinding.LayoutNoProductBinding
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

// 无产品
@AndroidEntryPoint
class NoProductFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(LayoutNoProductBinding::inflate)

    override fun contentView(): View {
        return  mBinding.root
    }

    override fun onPullToRefresh() {
        stopRefresh()
    }
}