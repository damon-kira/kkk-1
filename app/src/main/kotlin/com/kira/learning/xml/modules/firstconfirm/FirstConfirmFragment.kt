package com.kira.learning.xml.modules.firstconfirm

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.kira.learning.R
import com.kira.learning.databinding.FragmentFirstConfirmBinding
import com.kira.learning.dialog.CancelAutoHintDialog
import com.kira.learning.dialog.UploadDialog
import com.kira.learning.expand.ShowErrorMsg
import com.kira.learning.expand.getUnitString
import com.kira.learning.expand.maskBank
import com.kira.learning.expand.toast
import com.kira.learning.manager.Launch
import com.kira.learning.manager.Launch.jumpToAppSettingPage
import com.kira.learning.xml.modules.home.BaseHomeRefreshFragment
import com.kira.learning.xml.modules.home.HomeEvent
import com.kira.learning.xml.modules.home.vm.HomeLoanViewModel
import com.kira.learning.permission.PermissionHelper
import com.kira.learning.permission.appPermissions
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.kira.learning.xml.modules.firstconfirm.vm.AutoConfirmViewModel
import com.kira.learning.xml.modules.firstconfirm.vm.FirstConfirmViewModel
import com.util.lib.MainHandler
import com.util.lib.hide
import com.util.lib.log.logger_d
import com.util.lib.show
import com.util.lib.span.SpannableImpl
import com.util.lib.timeToTimeStr
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FirstConfirmFragment : BaseHomeRefreshFragment(), View.OnClickListener {

    private val mBinding by binding(FragmentFirstConfirmBinding::inflate)

    private val mViewModel by lazyViewModel<FirstConfirmViewModel>()

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

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
    private var mLoanAmount: String? = null// 仪表金额

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
        }
    }
}