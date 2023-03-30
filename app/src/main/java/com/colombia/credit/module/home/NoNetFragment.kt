package com.colombia.credit.module.home

import android.view.View
import com.colombia.credit.databinding.LayoutNoNetBinding
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoNetFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(LayoutNoNetBinding::inflate)

    override fun contentView(): View {
        return  mBinding.root
    }

    override fun onPullToRefresh() {
        stopRefresh()
    }
}