package com.colombia.credit.module.repay

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentTabRepayBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.inValidToken
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.linearLayoutManager
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.MainEvent
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

    private val mAdapter by lazy {
        RepayTabAdapter(arrayListOf(), mBinding.recyclerview).also {
            it.setHasStableIds(true)
        }
    }

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        mViewModel.getRepayOrders()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.group.referencedIds =
            intArrayOf(R.id.recyclerview, R.id.tv_repay, R.id.etv_repay_hint)
        setViewModelLoading(mViewModel)
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

        changePage(false)

        mViewModel.ordersLivedata.observerNonSticky(viewLifecycleOwner) {
            stopRefresh()
            if (!it.isSuccess()) {
                it.ShowErrorMsg {
                    mViewModel.getRepayOrders()
                }
            }
        }

        mViewModel.listLivedata.observerNonSticky(viewLifecycleOwner) { list ->
            changePage(!list.isNullOrEmpty())
            mAdapter.setItems(list ?: arrayListOf())
        }

        onPullToRefresh()

        mBinding.tvRepay.setBlockingOnClickListener {
            val list = mAdapter.getSelectorItems()
            if (list.isNotEmpty()) {
                // 调起支付
                list.map { it.W5KW6 }
                mAdapter.getTotalAmount()
                toast("调起支付，暂未处理")
            }
        }

        mBinding.emptyLayout.tvApply.setBlockingOnClickListener {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
        }
    }

    private fun changePage(isShowList: Boolean) {
        mBinding.group.ifShow(isShowList)
        mBinding.emptyLayout.llEmpty.ifShow(!isShowList)
        if (isShowList) {
            if (mBinding.etvRepayHint.top > 0) {
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
            }
            getBaseActivity()?.setStatusBarColor(Color.WHITE, true)
        }
    }
}