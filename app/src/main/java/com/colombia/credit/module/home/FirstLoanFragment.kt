package com.colombia.credit.module.home

import android.os.Bundle
import android.view.View
import com.colombia.credit.databinding.FragmentHomeLoanBinding
import com.colombia.credit.expand.*
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
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
            mFirstPageLoanAmount = mHomeViewModel.mRspInfoLiveData.value?.yqGhrjOF2.orEmpty()
            getProces()
        }
    }

    private fun getProces() {
        mHomeViewModel.getCertProcess()
    }
}