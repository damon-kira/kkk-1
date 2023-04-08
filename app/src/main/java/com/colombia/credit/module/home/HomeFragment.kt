package com.colombia.credit.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspProductInfo
import com.colombia.credit.databinding.FragmentHomeBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.inValidToken
import com.colombia.credit.expand.showAppUpgradeDialog
import com.colombia.credit.module.appupdate.AppUpdateViewModel
import com.colombia.credit.module.homerepay.FirstRepayFragment
import com.colombia.credit.module.login.LoginFragment
import com.colombia.credit.module.refused.RefusedFragment
import com.colombia.credit.module.repeat.RepeatFragment
import com.colombia.credit.module.review.ReviewFragment
import com.common.lib.base.BaseFragment
import com.common.lib.helper.FragmentHelper
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseHomeFragment(), IHomeFragment {

    private val mBinding by binding(FragmentHomeBinding::inflate)

    private var mCurrTag: String? = null

    private val mAppUpdateViewModel by lazyViewModel<AppUpdateViewModel>()

    private val mHomeViewModel by lazyViewModel<HomeLoanViewModel>()

    private val mLoginFragment by lazy {
        getInstance(getSupportContext(), LoginFragment::class.java, null)
    }
    private val mNoProductFragment by lazy {
        getInstance(getSupportContext(), NoProductFragment::class.java, null)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onRefresh() {
        mAppUpdateViewModel.getAppUpdate()
        mHomeViewModel.getHomeInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = if (inValidToken()) {
            mLoginFragment
        } else {
            mNoProductFragment
        }
        FragmentHelper.replaceFragment(
            childFragmentManager,
            R.id.fl_home_content,
            fragment,
            mCurrTag
        )

        LiveDataBus.getLiveData(HomeEvent::class.java).observerNonSticky(viewLifecycleOwner) {
            when (it.event) {
                HomeEvent.EVENT_REFRESH -> {
                    onRefresh()
                }
            }
        }

        mAppUpdateViewModel.updateLiveData.observerNonSticky(viewLifecycleOwner) {
            getBaseActivity()?.showAppUpgradeDialog(it)
        }
        mAppUpdateViewModel.getAppUpdate()

        mHomeViewModel.mHomeLiveData.observerNonSticky(viewLifecycleOwner) {
            stopRefresh()
            if (it.isSuccess()) {
                it.getData()?.let { data ->
                    val fragment = getCurrFragment(data)
                    if (fragment != null) {
                        replaceChildFragment(fragment)
                    }
                }
            } else {
                it.ShowErrorMsg()
            }
        }
        onRefresh()
    }

    private fun replaceChildFragment(fragment: BaseFragment) {
        FragmentHelper.replaceFragment(childFragmentManager, R.id.fl_home_content, fragment)
    }

    private fun getCurrFragment(rspInfo: RspProductInfo): BaseFragment? {
        val userStatus: String = rspInfo.EqyO
        val orderStatus: String = rspInfo.xXkO
        val firstProducts = rspInfo.fyEV
        return when (userStatus) {
            UserStatus.STATUS_FIRST -> {
                when (orderStatus) {
                    OrderStatus.STATUS_FIRST_PRODUCT,
                    OrderStatus.STATUS_REFUSED_EXPIRE -> {
                        if (firstProducts.isNullOrEmpty()) {
                            mNoProductFragment
                        } else {
                            getInstance(getSupportContext(), FirstLoanFragment::class.java, null)
                        }
                    }
                    OrderStatus.STATUS_REJECT -> {
                        getInstance(getSupportContext(), RefusedFragment::class.java, null)
                    }
                    OrderStatus.STATUS_REVIEW -> {
                        getInstance(getSupportContext(), ReviewFragment::class.java, null)
                    }
                    OrderStatus.STATUS_FIRST_CONFIRM -> {
                        getInstance(getSupportContext(), FirstRepayFragment::class.java, null)
                    }
                    OrderStatus.STATUS_REPAY,
                    OrderStatus.STATUS_OVERDUE -> {
                        getInstance(getSupportContext(), FirstRepayFragment::class.java, null)
                    }
                    else -> {
                        getInstance(getSupportContext(), NoProductFragment::class.java, null)
                    }
                }
            }
            UserStatus.STATUS_REPEAT,
            UserStatus.STATUS_BLACK -> {
                getInstance(getSupportContext(), RepeatFragment::class.java, null)
            }
            else -> null
        }
    }

    override fun getData(): RspProductInfo? = mHomeViewModel.mRspProductInfo

    override fun getHomeViewModel() = mHomeViewModel

    private fun stopRefresh() {
        val fragment = FragmentHelper.getCurrFragment(
            childFragmentManager,
            R.id.fl_home_content,
            mCurrTag
        )
        if (fragment is BaseHomeLoanFragment) {
            fragment.stopRefresh()
        }
    }
}