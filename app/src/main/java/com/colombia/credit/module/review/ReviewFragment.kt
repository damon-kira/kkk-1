package com.colombia.credit.module.review

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentReviewBinding
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.mUserName
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.HomeLoanViewModel
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