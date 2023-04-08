package com.colombia.credit.module.refused

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentRefusedBinding
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.home.IHomeFragment
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

//拒绝页面
@AndroidEntryPoint
class RefusedFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentRefusedBinding::inflate)

    private val mViewModel by lazyViewModel<HomeLoanViewModel>()

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    override fun onRefresh() {
        stopRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (parentFragment as? IHomeFragment)?.getData()?.let {
            setText(it.WTvE5G, it.yqGhrjOF2, it.K1v0Pz.orEmpty())
        }

        mViewModel.mRspInfoLiveData.observerNonSticky(this) {
            setText(it.WTvE5G, it.yqGhrjOF2, it.K1v0Pz.orEmpty())
        }
    }

    private fun setText(days: Int, amount: Int, date: String) {
        mBinding.layoutInfo.apply {
            tvDays.text = getString(R.string.days, days.toString())
            tvAmount.text = getString(R.string.amount_unit, formatCommon(amount.toString()))
            tvDesc.text = getString(R.string.refused_time, date)
        }
    }
}