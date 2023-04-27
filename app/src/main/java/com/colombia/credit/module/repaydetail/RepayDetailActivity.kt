package com.colombia.credit.module.repaydetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivityRepayDetailBinding
import com.colombia.credit.dialog.RepayDetailDialog
import com.colombia.credit.expand.*
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.defer.PayEvent
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.GsonUtil
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

// 首贷还款详情
@AndroidEntryPoint
class RepayDetailActivity : BaseActivity() {

    companion object {
        const val EXTRA_ID = "key_id"
    }

    private val mBinding by binding<ActivityRepayDetailBinding>()

    private val mViewModel by lazyViewModel<RepayDetailViewModel>()

    private var info: String = ""
    private var amount: String? = null
    private var mIds: String? = null

    private var loansonId = ""

    private val mObserver = { event: PayEvent ->
        Log.d(TAG, "event = $event")
        if (event.event == PayEvent.EVENT_REFRESH) {
            getDetail()
        }
    }

    private val detailDialog by lazy {
        RepayDetailDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(mBinding.root)
        setStatusBarColor(Color.WHITE, true)
        mIds = intent?.getStringExtra(EXTRA_ID)
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }
        mBinding.toolbar.setOnbackListener { finish() }

        mBinding.tvApply.setBlockingOnClickListener {
            Launch.skipWebViewActivity(
                this,
                H5UrlManager.getPayUrl(loansonId, amount.orEmpty(), "2")
            )
        }

        mBinding.tvAmount.setBlockingOnClickListener{
            detailDialog.show()
        }

        mBinding.tvExtension.setBlockingOnClickListener {
            Launch.skipDeferActivity(this, info)
        }

        LiveDataBus.getLiveData(PayEvent::class.java).observerNonSticky(this, mObserver)

        mViewModel.detailLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.let { data ->
                    changeEnable(true)
                    if (data.KUgC?.isNotEmpty() == true) {
                        val order = data.KUgC?.get(0) ?: return@observerNonSticky
                        amount = order.VJJxo2
                        mBinding.tvAmount.text = getUnitString(amount.orEmpty())
                        mBinding.tvApply.text = getString(
                            R.string.repay_amount_value,
                            getUnitString(amount.orEmpty())
                        )

                        info = GsonUtil.toJson(order).orEmpty()
                        loansonId = order.PJpH0.orEmpty()
                        mBinding.iilBankInfo.setRightText(order.DaNhMLH.orEmpty())
                        mBinding.iilDateInfo.setRightText(order.ch4x.orEmpty())
                        mBinding.iilRepayStatus.setRightText(order.wIWdNgC.orEmpty())
                        if (order.DlYbHlY == "1") {
                            mBinding.tvExtension.show()
                        }

                        // 还款明细弹窗
                        detailDialog.setDetail(
                            order.pHSUCa43.orEmpty(),
                            order.Dmj7UQm.orEmpty(),
                            order.cHum8.orEmpty(),
                            amount.orEmpty()
                        )
                    }
                }
            } else {
                changeEnable(false)
                it.ShowErrorMsg(::getDetail)
            }
        }
        getDetail()
    }

    private fun changeEnable(enable: Boolean) {
        mBinding.tvApply.isEnabled = enable
        mBinding.tvExtension.isEnabled = enable
    }

    private fun getDetail() {
        mViewModel.getDetail(mIds.orEmpty())
    }

    override fun onDestroy() {
        LiveDataBus.removeObserve(PayEvent::class.java, mObserver)
        super.onDestroy()
    }
}