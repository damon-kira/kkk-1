package com.colombia.credit.module.home

import android.os.Bundle
import android.view.View
import com.colombia.credit.databinding.LayoutNoProductBinding
import com.common.lib.livedata.LiveDataBus
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
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)
    }
}