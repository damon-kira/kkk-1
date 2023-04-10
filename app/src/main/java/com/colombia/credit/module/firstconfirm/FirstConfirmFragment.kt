package com.colombia.credit.module.firstconfirm

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentFirstConfirmBinding
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.IHomeFragment
import com.common.lib.expand.setBlockingOnClickListener
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

    override fun onPullToRefresh() {
        stopRefresh()
    }

//    val u5kCNqk: Int = 0//到手金额
//    val RIoDBuyjO: Int = 0//贷款金额,
//    val y5MbVG: Int = 0//期数
//    val b6O2Joc: Int = 0// 应还金额
//    val ihm3G2: String? = null//利息
//    val WTvE5G: String? = null//贷款周期  "7,14,90,108"
//    val vzXq3u:String? = null // 还款时间"2023-05-01"
//    val ZXEUWfOy: String? = null// 产品id
//    val XbCqhjDV:String? = null// 产品code

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (parentFragment as? IHomeFragment)?.getHomeViewModel()?.firstConfirmLiveData?.observe(
            viewLifecycleOwner
        ) {
            if (it.isNotEmpty()) {
                val firstConfirmInfo = it[0]
                // 显示账期
                 firstConfirmInfo.WTvE5G?.split(",")?.let {dateList ->
                     var isLock = false
                     for (s in dateList.take(4)) {
                         mBinding.rlPeriod1.setPeriod(s)
                         mBinding.rlPeriod1.showLock(isLock)
                         isLock = true
                     }
                 }
                mBinding.tvAmount.text = firstConfirmInfo.RIoDBuyjO
                mBinding.tvMin
                mBinding.tvMax
                mBinding.tvLoanValue.text = firstConfirmInfo.u5kCNqk
                mBinding.tvRepayValue.text = firstConfirmInfo.RIoDBuyjO
                mBinding.tvInterestValue.text = firstConfirmInfo.ihm3G2
                mBinding.tvBankNo
                mBinding.tvDateValue.text = firstConfirmInfo.vzXq3u
            }
        }

        mBinding.rlPeriod2.setBlockingOnClickListener(this)
        mBinding.rlPeriod3.setBlockingOnClickListener(this)
        mBinding.rlPeriod4.setBlockingOnClickListener(this)
        mBinding.tvBankNo.setBlockingOnClickListener(this)
        mBinding.confirmTvApply.setBlockingOnClickListener(this)
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
                Launch.skipBankCardListActivity(getSupportContext(), "3000")
            }
            R.id.confirm_tv_apply -> {

            }
        }
    }

}