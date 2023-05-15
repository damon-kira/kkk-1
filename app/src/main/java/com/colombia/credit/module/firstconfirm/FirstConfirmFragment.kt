package com.colombia.credit.module.firstconfirm

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bigdata.lib.loanPageStayTime
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentFirstConfirmBinding
import com.colombia.credit.dialog.CancelAutoHintDialog
import com.colombia.credit.dialog.UploadDialog
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.maskBank
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.Launch
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.upload.UploadViewModel
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.appPermissions
import com.colombia.credit.util.GPInfoUtils
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.MainHandler
import com.util.lib.hide
import com.util.lib.log.logger_d
import com.util.lib.show
import com.util.lib.span.SpannableImpl
import com.util.lib.timeToTimeStr
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

// 首贷确认额度页面
@AndroidEntryPoint
class FirstConfirmFragment : BaseHomeLoanFragment(), View.OnClickListener {

    private val mBinding by binding(FragmentFirstConfirmBinding::inflate)

    private val mViewModel by lazyViewModel<FirstConfirmViewModel>()

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private val mUploadViewModel by lazyViewModel<UploadViewModel>()

    private val mAutoConfirmModel by lazyViewModel<AutoConfirmViewModel>()

    override fun contentView(): View = mBinding.root

    private var time = 0L

    private var mLoanBankNo: String? = null
        set(value) {
            mBinding.tvBankNo.text = maskBank(value)
            field = value
        }
    private var mProductId: String? = null
    private var mProductCode: String? = null
    private var mLoanAmount: String? = null// 借款金额

    private val mProcessDialog by lazy {
        UploadDialog(getSupportContext())
    }

    override fun onPullToRefresh() {
        mHomeViewModel.getHomeInfo()
    }

    override fun onRefresh() {
        getTime()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(mAutoConfirmModel)
        time = System.currentTimeMillis()
        setCustomListener(mBinding.toolbar)
        initObserver()
        mBinding.rlPeriod2.setBlockingOnClickListener(this)
        mBinding.rlPeriod3.setBlockingOnClickListener(this)
        mBinding.rlPeriod4.setBlockingOnClickListener(this)
        mBinding.tvBankNo.setBlockingOnClickListener(this)
        mBinding.confirmTvApply.setBlockingOnClickListener(this)
        mBinding.aivJian.setBlockingOnClickListener(this)
        mBinding.aivPlus.setBlockingOnClickListener(this)
        mBinding.tvCancel.setBlockingOnClickListener(this)
    }

    private fun reqPermission() {
        PermissionHelper.reqPermission(
            getBaseActivity()!!,
            appPermissions.toList(),
            true,
            isFixGroup = true,
            {
                mProcessDialog.show()
                mUploadViewModel.checkAndUpload()
            },
            {
                getSupportContext().jumpToAppSettingPage()
            })
    }

    private fun initObserver() {
        setViewModelLoading(mAutoConfirmModel)
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
                mProductCode = firstConfirmInfo.XbCqhjDV
                getTime()
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
                GPInfoUtils.saveTag(GPInfoUtils.TAG8)
                mProcessDialog.end()
                MainHandler.postDelay({
                    mProcessDialog.dismiss()
                    mHomeViewModel.getHomeInfo()
                    Launch.skipApplySuccessActivity(getSupportContext())
                }, 260)
            } else {
                mProcessDialog.dismiss()
                it.ShowErrorMsg(::confirmLoan)
            }
        }

        LiveDataBus.getLiveData(ConfirmEvent::class.java).observerNonSticky(viewLifecycleOwner) {
            if (it.event == ConfirmEvent.EVENT_BANK_NO) {
                if (!it.value.isNullOrEmpty()) {
                    mLoanBankNo = it.value
                }
            }
        }

        mUploadViewModel.resultLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                confirmLoan()
            } else {
                mProcessDialog.dismiss()
                it.ShowErrorMsg(::reqPermission)
            }
        }

        mAutoConfirmModel.downTimerLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it < 0) {
                logger_d(TAG, "initObserver: time == $it")
                // 获取数据，自动确认额度接口
                mBinding.tvAuto.hide()
                mBinding.tvCancel.hide()
                reqPermission()
            } else {
                mBinding.tvAuto.show()
                mBinding.tvCancel.show()
                val param = timeToTimeStr(it, TimeUnit.SECONDS,true)
                val text = getString(R.string.auto_confirm_hint, param)
                val span = SpannableImpl().init(text)
                    .color(ContextCompat.getColor(getSupportContext(), R.color.color_FE4F4F), param)
                    .getSpannable()
                mBinding.tvAuto.text = span
            }
        }

        mAutoConfirmModel.cancelLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                mAutoConfirmModel.stopCountDown()
                mBinding.tvAuto.hide()
                mBinding.tvCancel.hide()
            } else it.ShowErrorMsg(::cancelLoan)
        }
    }

    private fun getTime() {
        mAutoConfirmModel.getDownTimeMill("", mProductCode.orEmpty())
    }

    private fun cancelLoan(){
        mAutoConfirmModel.cancel("", mProductId.orEmpty())
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
                reqPermission()
            }
            R.id.aiv_jian -> {
                toast(R.string.toast_mix_amount)
            }
            R.id.aiv_plus -> {
                toast(R.string.toast_max_amount)
            }
            R.id.tv_cancel -> {
                CancelAutoHintDialog(getSupportContext())
                    .setAmount(mLoanAmount.orEmpty())
                    .setConfirmListener {
                        cancelLoan()
                    }
                    .show()
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

    private var startTime = 0L
    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if (visible) {
            startTime = System.currentTimeMillis()
        } else {
            loanPageStayTime = System.currentTimeMillis() - startTime
        }
    }
}