package com.kira.learning.module.homerepay

import android.os.Bundle
import android.view.View
import com.kira.learning.R
import com.kira.learning.databinding.FragmentHomeRepayBinding
import com.kira.learning.expand.formatCommon
import com.kira.learning.expand.mUserName
import com.kira.learning.manager.Launch
import com.kira.learning.module.defer.PayEvent
import com.kira.learning.module.home.BaseHomeLoanFragment
import com.kira.learning.module.home.HomeEvent
import com.kira.learning.module.home.vm.HomeLoanViewModel
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstRepayFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentHomeRepayBinding::inflate)

    private val mHomeLoanViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    private var mIds: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.repayToolbar)
        mBinding.tvUser.text = getString(R.string.hi_user, mUserName)
        mBinding.repayTvRepay.setBlockingOnClickListener {
            if (mIds.isNullOrEmpty()) {
                onPullToRefresh()
                return@setBlockingOnClickListener
            }
            Launch.skipRepayDetailActivity(getSupportContext(), mIds.orEmpty())
        }
        mHomeLoanViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            mBinding.repayTvAmount.apply {
                text = getString(R.string.amount_unit, formatCommon(it.yqGhrjOF2.orEmpty()))
                isSelected = it.v3ItXF > 0 // 是否逾期
            }
            mBinding.tvRepaydate.text = it.vzXq3u
            mIds = it.ZXEUWfOy
            if (it.v3ItXF > 0) {
                mBinding.etvOverdue.show()
                mBinding.etvOverdue.text = getString(R.string.overdue_tag, it.v3ItXF.toString())
            }
        }

        LiveDataBus.getLiveData(PayEvent::class.java).observerNonSticky(viewLifecycleOwner) {
            if (it.event == PayEvent.EVENT_REFRESH) {
                onPullToRefresh()
            }
        }
    }
}