package com.kira.learning.xml.modules.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.common.lib.base.BaseFragment
import com.common.lib.helper.FragmentHelper
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.bean.res.RspProductInfo
import com.kira.learning.databinding.FragmentHomeBinding
import com.kira.learning.expand.ShowErrorMsg
import com.kira.learning.expand.inValidToken
import com.kira.learning.expand.showAppUpgradeDialog
import com.kira.learning.xml.modules.appupdate.AppUpdateViewModel
import com.kira.learning.xml.modules.firstconfirm.FirstConfirmFragment
import com.kira.learning.xml.modules.home.vm.HomeLoanViewModel
import com.kira.learning.xml.modules.login.LoginFragment
import com.kira.learning.xml.modules.navigation.NavigationFragment
import com.kira.learning.xml.modules.navigation.RefusedFragment
import com.kira.learning.xml.modules.review.ReviewFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class HomeFragment : BaseHomeFragment() {

    private val mBinding by binding(FragmentHomeBinding::inflate)

    private var mCurrTag: String? = null

    private val mAppUpdateViewModel by lazyViewModel<AppUpdateViewModel>()

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private val mLoginFragment
        get() = getInstance(getSupportContext(), LoginFragment::class.java, null)

    private var noProductFragmentRef: WeakReference<NoProductFragment>? = null

    val mNoProductFragment: NoProductFragment
        get() = noProductFragmentRef?.get() ?: run {
            getInstance(
                getSupportContext(),
                NoProductFragment::class.java,
                null
            ).also { noProductFragmentRef = WeakReference(it) }
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
        if (inValidToken()) {
            stopRefresh()
            return
        }
        mHomeViewModel.getHomeInfo()
    }

    private val mHomeEventObserver = { event: HomeEvent ->
        when (event.event) {
            HomeEvent.EVENT_REFRESH -> {
                onRefresh()
            }

            HomeEvent.EVENT_LOGIN -> {
                onRefresh()
                replaceChildFragment(getFragment())
            }

            HomeEvent.EVENT_LOGOUT -> {
                mHomeViewModel.clearData()
                replaceChildFragment(getFragment())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e(TAG, "onViewCreated: 调用了")
        super.onViewCreated(view, savedInstanceState)
        //首次创建
        replaceChildFragment(getFragment())

        LiveDataBus.getLiveData(HomeEvent::class.java)
            .observerNonSticky(viewLifecycleOwner, mHomeEventObserver)

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

    override fun onTouchEvent(ev: MotionEvent?) {
        getCurrChildFragment()?.onTouchEvent(ev)
    }

    private fun getFragment(): BaseHomeFragment {
        val fragment = if (inValidToken()) {
            mLoginFragment
        } else {
            mNoProductFragment
        }
        return fragment
    }

    private fun replaceChildFragment(fragment: BaseFragment) {
        FragmentHelper.replaceFragment(childFragmentManager, R.id.fl_home_content, fragment)
    }

    private fun getCurrChildFragment(): BaseFragment? {
        return FragmentHelper.getCurrFragment(childFragmentManager, R.id.fl_home_content, mCurrTag)
    }

    private fun getCurrFragment(rspInfo: RspProductInfo): BaseFragment? {
        val userStatus: String = "2"//rspInfo.EqyO
        val orderStatus: String = rspInfo.xXkO
        return when (userStatus) {
            UserStatus.STATUS_FIRST -> {
                when (orderStatus) {
                    OrderStatus.STATUS_FIRST_PRODUCT,
                    OrderStatus.STATUS_REFUSED_EXPIRE -> {
                        if (rspInfo.yqGhrjOF2 == "0") {
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
                        getInstance(getSupportContext(), FirstConfirmFragment::class.java, null)
                    }

                    else -> {
                        getInstance(getSupportContext(), NoProductFragment::class.java, null)
                    }
                }
            }

            UserStatus.STATUS_NAVIGATION -> {
                getInstance(getSupportContext(), NavigationFragment::class.java, null)
            }

            else -> null
        }
    }

    private fun stopRefresh() {
        val fragment = FragmentHelper.getCurrFragment(
            childFragmentManager,
            R.id.fl_home_content,
            mCurrTag
        )
        if (fragment is BaseHomeRefreshFragment) {
            fragment.stopRefresh()
        }
    }

    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if (visible) {
            if (inValidToken()) {
                replaceChildFragment(getFragment())
            }
        }
    }

    override fun onDestroyView() {
        noProductFragmentRef?.clear()
        noProductFragmentRef = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        LiveDataBus.removeObserve(HomeEvent::class.java, mHomeEventObserver)
        super.onDestroy()
    }
}