package com.colombia.credit.module.repay

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentTabRepayBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.inValidToken
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.linearLayoutManager
import com.colombia.credit.module.defer.PayEvent
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.MainEvent
import com.colombia.credit.permission.HintDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.GsonUtil
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.dp
import com.util.lib.ifShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepayTabFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentTabRepayBinding::inflate)

    private val mViewModel by lazyViewModel<RepayTabViewModel>()

    private val mCheckViewModel by lazyViewModel<RepayCheckViewModel>()

    private val mAdapter by lazy {
        RepayTabAdapter(arrayListOf(), mBinding.recyclerview).also {
            it.setHasStableIds(true)
        }
    }

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        // check token
//        if (inValidToken()) {
//            stopRefresh()
//            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
//            return
//        }

        mViewModel.getRepayOrders()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.group.referencedIds =
            intArrayOf(R.id.recyclerview, R.id.tv_repay, R.id.etv_repay_hint)
        setViewModelLoading(mViewModel)
        mBinding.emptyLayout.tvEmptyText.setText(R.string.repay_empty)
        // init recyclerview
        mBinding.recyclerview.linearLayoutManager()
        mBinding.recyclerview.adapter = mAdapter

        mBinding.tvRepay.text = getString(R.string.tab_repay_btn, "0")

        mAdapter.mExtensionListener = {
            Launch.skipRepayDeferActivity(getSupportContext(), GsonUtil.toJson(it).orEmpty())
        }
        mAdapter.mSelectListener = {
            val amount = getUnitString(mAdapter.getTotalAmount().toString())
            mBinding.tvRepay.text = getString(R.string.tab_repay_btn, amount)
        }
        mAdapter.mOnItemClick = { detail, position ->
            if (position == 0) {
                Launch.skipPhotographActivity(getSupportContext())
            } else {
                Launch.skipRepayDetailActivity(getSupportContext(), detail.bS6qpg4E.orEmpty())
            }
        }

        initObserver()
        if (!inValidToken()) {
            mViewModel.getRepayOrders()
        }

        mBinding.tvRepay.setBlockingOnClickListener {
            Launch.skipCourseActivity(getSupportContext())
//            checkOrder()
        }
        // Click
        mBinding.emptyLayout.tvApply.setBlockingOnClickListener {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))

        }

        LiveDataBus.getLiveData(PayEvent::class.java).observerNonSticky(viewLifecycleOwner) {
            if (it.event == PayEvent.EVENT_REFRESH) {
                onPullToRefresh()
            }
        }
    }

    private fun checkOrder() {
        val list = mAdapter.getSelectorItems()
        if (list.isNotEmpty()) {
            val loanIds = list.map { it.W5KW6 }
            mCheckViewModel.checkStatus(loanIds.joinToString(","))
        }
    }

    private fun initObserver() {
        setViewModelLoading(mCheckViewModel)
        mViewModel.ordersLivedata.observerNonSticky(viewLifecycleOwner) {
            stopRefresh()
            if (!it.isSuccess()) {
                it.ShowErrorMsg {
                    mViewModel.getRepayOrders()
                }
            }
        }

        mViewModel.listLivedata.observerNonSticky(viewLifecycleOwner) { list ->
            mAdapter.setItems(list ?: arrayListOf())
            changePage(!list.isNullOrEmpty())
        }

        LiveDataBus.getLiveData(HomeEvent::class.java).observerNonSticky(viewLifecycleOwner) {
            if (it.event == HomeEvent.EVENT_LOGOUT) {
                changePage(false)
                mViewModel.clearData()
            } else if (it.event == HomeEvent.EVENT_LOGIN) {
                onPullToRefresh()
            }
        }

        mCheckViewModel.mCheckLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                if (it.getData()?.oasdnjuxnjas == true) {
                    // 调起支付
                    Launch.skipWebViewActivity(
                        getSupportContext(), H5UrlManager.getPayUrl(
                            mAdapter.getSelectIds(), mAdapter.getTotalAmount().toString(), "2"
                        )
                    )
                } else {
                    val dialog =
                        HintDialog(getSupportContext()).showTitle(HintDialog.TYPE_INVISIBLE)
                            .setMessage(getString(R.string.repay_success3))
                            .setBtnText(getString(R.string.confirm)).showClose(false)
                            .setOnClickListener {
                                onPullToRefresh()
                            }
                    getBaseActivity()?.addDialog(dialog)
                }
            } else it.ShowErrorMsg(::checkOrder)
        }
    }

    /**
     * page check
     */
    private fun changePage(isShowList: Boolean) {
        mBinding.group.ifShow(isShowList)
        mBinding.emptyLayout.llEmpty.ifShow(!isShowList)
        if (isShowList && mAdapter.itemCount > 3) {
            if (mBinding.etvRepayHint.top > 100) {
                changeListPadding(mBinding.etvRepayHint.top)
            } else {
                mBinding.etvRepayHint.post {
                    if (isDestroyView()) return@post
                    changeListPadding(mBinding.etvRepayHint.top)
                }
            }
        }
    }

    private fun changeListPadding(offset: Int) {
        val padding = mBinding.clContent.height - offset + 20f.dp()
        mBinding.recyclerview.setPadding(
            mBinding.recyclerview.paddingLeft,
            mBinding.recyclerview.paddingTop,
            mBinding.recyclerview.paddingRight,
            padding
        )
    }

    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if (visible) {
            if (inValidToken()) {
                changePage(false)
            } else {
                onPullToRefresh()
            }
            getBaseActivity()?.setStatusBarColor(Color.WHITE, true)
        }
    }
}