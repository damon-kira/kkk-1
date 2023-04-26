package com.colombia.credit.module.home

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bigdata.lib.bean.BaseInfo
import com.colombia.credit.databinding.FragmentHomeLoanBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.jumpProcess
import com.colombia.credit.expand.mFirstPageLoanAmount
import com.colombia.credit.manager.Launch
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.GsonUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FirstLoanFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentHomeLoanBinding::inflate)

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    override fun contentView(): View {
        return mBinding.root
    }

    override fun onPullToRefresh() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    override fun onRefresh() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)
        setViewModelLoading(mHomeViewModel)
        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            mBinding.tvMaxAmount.text = getUnitString(it.yqGhrjOF2.orEmpty())
        }

        mHomeViewModel.mCertProcessLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                it.getData()?.let { info ->
                    jumpProcess(getSupportContext(), info.getProcessType())
                }
            } else it.ShowErrorMsg(::getProces)
        }

        mBinding.loanTvApply.setBlockingOnClickListener {
//            mFirstPageLoanAmount = mHomeViewModel.mRspInfoLiveData.value?.yqGhrjOF2.orEmpty()
//            getProces()
            Launch.skipFaceActivity(getSupportContext())
        }
    }

    private fun getProces() {
        mHomeViewModel.getCertProcess()
    }
}