package com.colombia.credit.module.homerepay

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentHomeRepayBinding
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.expand.getUserName
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.IHomeFragment
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstRepayFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentHomeRepayBinding::inflate)

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.repayToolbar)
        mBinding.tvUser.text = getString(R.string.hi_user, getUserName())
        mBinding.repayTvRepay.setBlockingOnClickListener {
            // 跳转还款详情页面
            Launch.skipRepayDetailActivity(getSupportContext())
        }
        (parentFragment as? IHomeFragment)?.getHomeViewModel()?.mRspInfoLiveData?.observe(viewLifecycleOwner) {
            mBinding.repayTvAmount.apply {
                text = getString(R.string.amount_unit, formatCommon(it.yqGhrjOF2.orEmpty()))
                isSelected = it.v3ItXF > 0 // 是否逾期
            }

            // vzXq3u 还款日期
            mBinding.tvRepaydate.text = it.vzXq3u
            if (it.v3ItXF > 0){
                mBinding.etvOverdue.show()
                mBinding.etvOverdue.text = getString(R.string.overdue_tag, it.v3ItXF.toString())
            }
        }
    }
}