package com.kira.learning.module.review

import android.os.Bundle
import android.view.View
import com.kira.learning.R
import com.kira.learning.databinding.FragmentReviewBinding
import com.kira.learning.expand.getUnitString
import com.kira.learning.expand.mUserName
import com.kira.learning.module.home.BaseHomeLoanFragment
import com.kira.learning.module.home.HomeEvent
import com.kira.learning.module.home.vm.HomeLoanViewModel
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

// 待审核
@AndroidEntryPoint
class ReviewFragment : BaseHomeLoanFragment() {

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