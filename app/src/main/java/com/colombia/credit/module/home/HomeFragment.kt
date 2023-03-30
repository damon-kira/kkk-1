package com.colombia.credit.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentHomeBinding
import com.colombia.credit.expand.inValidToken
import com.colombia.credit.module.login.LoginFragment
import com.colombia.credit.module.review.ReviewFragment
import com.common.lib.helper.FragmentHelper
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseHomeFragment() {

    private val mBinding by binding(FragmentHomeBinding::inflate)

    private var mCurrTag: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    private val mLoginFragment by lazy {
        getInstance(context!!, LoginFragment::class.java, null)
    }

    private val mHomeLoanFragment by lazy {
        getInstance(context!!, HomeLoanFragment::class.java, null)
    }

    private val mReviewFragment by lazy {
        getInstance(context!!, ReviewFragment::class.java, null)
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
    }
}