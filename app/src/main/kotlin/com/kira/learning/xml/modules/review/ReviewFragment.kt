package com.kira.learning.xml.modules.review

import android.os.Bundle
import android.view.View
import com.kira.learning.R
import com.kira.learning.databinding.FragmentReviewBinding
import com.kira.learning.xml.expand.getUnitString
import com.kira.learning.xml.expand.mUserName
import com.kira.learning.xml.modules.home.BaseHomeRefreshFragment
import com.kira.learning.xml.modules.home.HomeEvent
import com.kira.learning.xml.modules.home.vm.HomeLoanViewModel
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

// 待审核
@AndroidEntryPoint
class ReviewFragment : BaseHomeRefreshFragment() {

    private val mBinding by binding(FragmentReviewBinding::inflate)

    private val mHomeLoanViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.reviewToolbar)
        mBinding.tvUser.text = getString(R.string.hi_user, mUserName)
        mHomeLoanViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            mBinding.reviewTvAmount.text = getUnitString(it.yqGhrjOF2.orEmpty())
        }
    }
}