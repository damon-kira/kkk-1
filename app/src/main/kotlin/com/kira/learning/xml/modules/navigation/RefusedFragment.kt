package com.kira.learning.xml.modules.navigation

import android.os.Bundle
import android.view.View
import com.kira.learning.R
import com.kira.learning.databinding.FragmentRefusedBinding
import com.kira.learning.xml.expand.formatCommon
import com.kira.learning.xml.modules.home.BaseHomeRefreshFragment
import com.kira.learning.xml.modules.home.HomeEvent
import com.kira.learning.xml.modules.home.vm.HomeLoanViewModel
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

//拒绝页面
@AndroidEntryPoint
class RefusedFragment : BaseHomeRefreshFragment() {

    private val mBinding by binding(FragmentRefusedBinding::inflate)


    private val mViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    override fun onRefresh() {
        stopRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)
        mViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            setText(it.RdJ7nJ.orEmpty(), it.yqGhrjOF2.orEmpty(), it.K1v0Pz.orEmpty())
        }
    }

    private fun setText(days: String, amount: String, date: String) {
        mBinding.layoutInfo.apply {
            tvDays.text = days
            tvAmount.text = getString(R.string.amount_unit, formatCommon(amount))
            tvDesc.text = getString(R.string.refused_time, date)
        }
    }
}