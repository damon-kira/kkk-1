package com.colombia.credit.module.repaydetail

import android.graphics.Color
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivityRepayDetailBinding
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

    private val mObserver = { event: PayEvent ->
        if (event.event == PayEvent.EVENT_REFRESH) {
            getDetail()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBarColor(Color.WHITE, true)
        mIds = intent?.getStringExtra(EXTRA_ID)
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }

        mBinding.tvApply.setBlockingOnClickListener {
            Launch.skipWebViewActivity(
                this,
                H5UrlManager.getPayUrl(mIds.orEmpty(), amount.orEmpty(), "2")
            )
        }

        mBinding.tvExtension.setBlockingOnClickListener {
            Launch.skipDeferActivity(this, info)
        }

        LiveDataBus.getLiveData(PayEvent::class.java).observerNonSticky(this, mObserver)

        mViewModel.detailLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.let { data ->
                    amount = data.TxksJTU8C
                    mBinding.tvAmount.text = getUnitString(data.TxksJTU8C.orEmpty())
                    mBinding.tvApply.text = getString(
                        R.string.repay_amount_value,
                        getUnitString(data.TxksJTU8C.orEmpty())
                    )
                    val order = data.KUgC?.get(0) ?: return@observerNonSticky
                    info = GsonUtil.toJson(order).orEmpty()
                    mBinding.iilBankInfo.setRightText(order.DaNhMLH.orEmpty())
                    mBinding.iilDateInfo.setRightText(order.ch4x.orEmpty())
                    mBinding.iilRepayStatus.setRightText(order.wIWdNgC.orEmpty())
                    if (order.DlYbHlY == "1") {
                        mBinding.tvExtension.show()
                    }
                }
            } else it.ShowErrorMsg(::getDetail)
        }
        getDetail()
    }

    private fun getDetail() {
        mViewModel.getDetail(mIds.orEmpty())
    }

    override fun onDestroy() {
        LiveDataBus.removeObserve(PayEvent::class.java, mObserver)
        super.onDestroy()
    }
}