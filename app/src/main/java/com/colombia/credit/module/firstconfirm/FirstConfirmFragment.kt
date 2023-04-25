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
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.service.SerManager
import com.colombia.credit.module.upload.UploadViewModel
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.appPermissions
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

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private val mUploadViewModel by lazyViewModel<UploadViewModel>()

    override fun contentView(): View = mBinding.root

    private var mLoanBankNo: String? = null
        set(value) {
            mBinding.tvBankNo.text = maskString(value.orEmpty(), 2, 2)
            field = value
        }
    private var mProductId: String? = null
    private var mLoanAmount: String? = null// 借款金额

    override fun onPullToRefresh() {
        mHomeViewModel.getHomeInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelLoading(mViewModel)
        setViewModelLoading(mUploadViewModel)
        setCustomListener(mBinding.toolbar)
        initObserver()

        PermissionHelper.reqPermission(getBaseActivity()!!, appPermissions.toList(), true, {
            SerManager.uploadData()
        }, {
            getSupportContext().jumpToAppSettingPage()
        })

        mBinding.rlPeriod2.setBlockingOnClickListener(this)
        mBinding.rlPeriod3.setBlockingOnClickListener(this)
        mBinding.rlPeriod4.setBlockingOnClickListener(this)
        mBinding.tvBankNo.setBlockingOnClickListener(this)
        mBinding.confirmTvApply.setBlockingOnClickListener(this)
        mBinding.aivJian.setBlockingOnClickListener(this)
        mBinding.aivPlus.setBlockingOnClickListener(this)
    }

    private fun initObserver() {
        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) { info ->
            mBinding.tvMax.text = getUnitString(info.yqGhrjOF2.orEmpty())
            mLoanBankNo = info.yMiEwn3
            val list = info.fyEV
            if (list?.isNotEmpty() == true) {
                val firstConfirmInfo = list[0]
                // 显示账期
                firstConfirmInfo.WTvE5G?.split(",")?.let { dateList ->
                    var isLock = false
                    for (index in 0 until dateList.take(4).size) {
                        val s = dateList[index]
                        val view = when (index) {
                            0 -> {
                                mBinding.rlPeriod1
                            }
                            1 -> {
                                mBinding.rlPeriod2
                            }
                            2 -> {
                                mBinding.rlPeriod3
                            }
                            else -> {
                                mBinding.rlPeriod4
                            }
                        }
                        view.showLock(isLock)
                        view.setPeriod(s)
                        isLock = true
                    }
                }

                mProductId = firstConfirmInfo.ZXEUWfOy
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
                mHomeViewModel.getHomeInfo()
                Launch.skipApplySuccessActivity(getSupportContext())
            } else it.ShowErrorMsg(::confirmLoan)
        }

        LiveDataBus.getLiveData(ConfirmEvent::class.java).observerNonSticky(viewLifecycleOwner) {
            if (it.event == ConfirmEvent.EVENT_BANK_NO) {
                if (!it.value.isNullOrEmpty()) {
                    mLoanBankNo = it.value
                }
            }
        }

        mUploadViewModel.resultLiveData.observerNonSticky(viewLifecycleOwner) {
            confirmLoan()
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
                    mProductId.orEmpty(),
                    mLoanBankNo.orEmpty()
                )
            }
            R.id.confirm_tv_apply -> {
                mUploadViewModel.checkAndUpload()
            }
            R.id.aiv_jian -> {
                toast(R.string.toast_mix_amount)
            }
            R.id.aiv_plus -> {
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