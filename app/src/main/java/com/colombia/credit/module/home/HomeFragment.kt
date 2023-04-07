package com.colombia.credit.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentHomeBinding
import com.colombia.credit.expand.inValidToken
import com.colombia.credit.expand.showAppUpgradeDialog
import com.colombia.credit.module.appupdate.AppUpdateViewModel
import com.colombia.credit.module.firstconfirm.FirstConfirmFragment
import com.colombia.credit.module.login.LoginFragment
import com.colombia.credit.module.review.ReviewFragment
import com.common.lib.helper.FragmentHelper
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseHomeFragment() {

    private val mBinding by binding(FragmentHomeBinding::inflate)

    private var mCurrTag: String? = null

    private val mViewModel by lazyViewModel<AppUpdateViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onRefresh() {
        mViewModel.getAppUpdate()
    }

    private val mLoginFragment by lazy {
        getInstance(getSupportContext(), LoginFragment::class.java, null)
    }

    private val mHomeLoanFragment by lazy {
        getInstance(getSupportContext(), HomeLoanFragment::class.java, null)
    }

    private val mReviewFragment by lazy {
        getInstance(getSupportContext(), ReviewFragment::class.java, null)
    }

    private val mFirstConfirmFragment by lazy {
        getInstance(getSupportContext(), FirstConfirmFragment::class.java, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = if (inValidToken()) {
            mLoginFragment
        } else {
            mReviewFragment
        }
        FragmentHelper.repleaceFragment(
            childFragmentManager,
            R.id.fl_home_content,
            fragment,
            mCurrTag
        )

        mViewModel.updateLiveData.observerNonSticky(viewLifecycleOwner) {
            getBaseActivity()?.showAppUpgradeDialog(it)
        }
        mViewModel.getAppUpdate()
    }
}