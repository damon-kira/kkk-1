package com.colombia.credit.module.firstconfirm

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentFirstConfirmBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.maskString
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.IHomeFragment
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 首贷确认额度页面
 */
@AndroidEntryPoint
class FirstConfirmFragment : BaseHomeLoanFragment(), View.OnClickListener {

    private val mBinding by binding(FragmentFirstConfirmBinding::inflate)

    private val mViewModel by lazyViewModel<FirstConfirmViewModel>()

    override fun contentView(): View = mBinding.root

    private var mLoanBankNo: String? = null
    private var mProductId: String? = null
    private var mLoanAmount: String? = null// 借款金额

    override fun onPullToRefresh() {
        stopRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelLoading(mViewModel)
        initObserver()

        mBinding.rlPeriod2.setBlockingOnClickListener(this)
        mBinding.rlPeriod3.setBlockingOnClickListener(this)
        mBinding.rlPeriod4.setBlockingOnClickListener(this)
        mBinding.tvBankNo.setBlockingOnClickListener(this)
        mBinding.confirmTvApply.setBlockingOnClickListener(this)
        mBinding.tvMin.setBlockingOnClickListener(this)
        mBinding.tvMax.setBlockingOnClickListener(this)
    }

    private fun initObserver() {
        (parentFragment as? IHomeFragment)?.getHomeViewModel()?.mRspInfoLiveData?.observe(
            viewLifecycleOwner
        ) { info ->
            mBinding.tvMax.text = getUnitString(info.yqGhrjOF2.orEmpty())
            mBinding.tvBankNo.text = maskString(info.yMiEwn3, 2, 2)
            val list = info.fyEV
            if (list?.isNotEmpty() == true) {
                val firstConfirmInfo = list[0]
                // 显示账期
                firstConfirmInfo.WTvE5G?.split(",")?.let { dateList ->
                    var isLock = false
                    for (s in dateList.take(4)) {
                        mBinding.rlPeriod1.setPeriod(s)
                        mBinding.rlPeriod1.showLock(isLock)
                        isLock = true
                    }
                }

                mLoanAmount = firstConfirmInfo.RIoDBuyjO
                val amount = getUnitString(firstConfirmInfo.RIoDBuyjO.orEmpty())
                mBinding.tvAmount.text = amount
                mBinding.tvMin.text = amount
                mBinding.tvLoanValue.text = getUnitString(firstConfirmInfo.u5kCNqk.orEmpty())
                mBinding.tvRepayValue.text = getUnitString(firstConfirmInfo.b6O2Joc.orEmpty())
                mBinding.tvInterestValue.text = getUnitString(firstConfirmInfo.ihm3G2.orEmpty())
                mBinding.tvDateValue.text = firstConfirmInfo.vzXq3u
            }
        }

        mViewModel.confirmLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
            } else it.ShowErrorMsg(::confirmLoan)
        }

        LiveDataBus.getLiveData(ConfirmEvent::class.java).observerNonSticky(viewLifecycleOwner) {
            if (it.event == ConfirmEvent.EVENT_BANK_NO) {
                if (!it.value.isNullOrEmpty()) {
                    mLoanBankNo = it.value
                }
            }
        }
    }


    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.rl_period2,
            R.id.rl_period3,
            R.id.rl_period4 -> {
                toast(R.string.toast_level_hint)
            }
            R.id.tv_bank_no -> {
                Launch.skipBankCardListActivity(
                    getSupportContext(),
                    mLoanAmount.orEmpty(),
                    mProductId.orEmpty()
                )
            }
            R.id.confirm_tv_apply -> {
                confirmLoan()
            }
            R.id.tv_min -> {
                toast(R.string.toast_mix_amount)
            }
            R.id.tv_max -> {
                toast(R.string.toast_max_amount)
            }
        }
    }

    private fun confirmLoan() {
        if (mLoanBankNo.isNullOrEmpty() || mProductId.isNullOrEmpty()) {
            LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
            return
        }
        mViewModel.confirmLoan(mLoanBankNo.orEmpty(), mProductId.orEmpty())
    }

}