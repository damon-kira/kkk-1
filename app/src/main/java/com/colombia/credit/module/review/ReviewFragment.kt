package com.colombia.credit.module.review

import android.os.Bundle
import android.view.View
import com.colombia.credit.databinding.FragmentReviewBinding
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

// 待审核
@AndroidEntryPoint
class ReviewFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentReviewBinding::inflate)

    override fun contentView(): View = mBinding.root


    override fun onPullToRefresh() {
        stopRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.homeToolbar.showCustomIcon(true)
    }
}