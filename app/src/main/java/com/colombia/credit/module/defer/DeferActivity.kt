package com.colombia.credit.module.defer

import android.graphics.Color
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspRepayDetail
import com.colombia.credit.databinding.ActivityDeferBinding
import com.colombia.credit.dialog.ExtensionConfirmDialog
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.showCustomDialog
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.repay.RepayCheckViewModel
import com.colombia.credit.permission.HintDialog
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.GsonUtil
import com.util.lib.StatusBarUtil.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

// 展期
@AndroidEntryPoint
open class DeferActivity : BaseActivity() {

    companion object {
        const val EXTRA_INFO = "info"
    }

    protected val mBinding by binding<ActivityDeferBinding>()
    protected var orderId: String = ""
    protected var jine: String = ""

    protected var mLoanId: String = ""

    private val mCheckViewModel by lazyViewModel<RepayCheckViewModel>()

    private val mObserver = { payEvent: PayEvent ->
        if (payEvent.event == PayEvent.EVENT_EXIT) {
            LiveDataBus.post(PayEvent(PayEvent.EVENT_REFRESH))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(Color.WHITE, true)
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }
        mBinding.toolbar.setOnbackListener {
            finish()
        }

        getInfo()

        mBinding.tvApply.setBlockingOnClickListener {
            // 调用支付
            ExtensionConfirmDialog(this).setOnClickListener {
                Launch.skipWebViewActivity(this, H5UrlManager.getPayUrl(orderId, jine, "1"))
            }.show()
        }

        LiveDataBus.getLiveData(PayEvent::class.java).observerNonSticky(this, mObserver)

        mCheckViewModel.mCheckLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                if(it.getData()?.oasdnjuxnjas == true) {
                    Launch.skipWebViewActivity(
                        this,
                        H5UrlManager.getPayUrl(orderId, jine, "2")
                    )
                } else {
                    val dialog = HintDialog(this).showTitle(HintDialog.TYPE_INVISIBLE)
                        .setMessage(getString(R.string.repay_success2))
                        .setBtnText(getString(R.string.confirm))
                        .showClose(false)
                        .setOnClickListener {
                            checkOrderRepay()
                        }
                    addDialog(dialog)

                }
            } else it.ShowErrorMsg(::checkOrder)
        }
    }


    private fun checkOrder(){
        mCheckViewModel.checkStatus(orderId)
    }

    override fun onDestroy() {
        LiveDataBus.removeObserve(PayEvent::class.java, mObserver)
        super.onDestroy()
    }

    open fun getInfo() {
        intent?.getStringExtra(EXTRA_INFO)?.let {
            val detail = GsonUtil.fromJsonNew<RspRepayDetail.RepayDetail>(it)
            jine = detail?.EA7nMOa.orEmpty()
            val amount = getUnitString(jine)
            mBinding.tvAmount.text = amount
            mBinding.iilDays.setRightText(getString(R.string.days, detail?.zlftJgf ?: "--"))
            mBinding.iilDate.setRightText(detail?.YXtMfL6nAm.orEmpty())
            mBinding.iilAmount.setRightText(getUnitString(detail?.dbhmAOVp56.orEmpty()))
            mBinding.tvApply.text = getString(R.string.repay_amount_value, amount)
            orderId = detail?.PJpH0.orEmpty()
            mLoanId = detail?.KER10faeq9.orEmpty()
        }
    }

    open fun checkOrderRepay() {
        LiveDataBus.post(PayEvent(PayEvent.EVENT_REFRESH))
        finish()
    }
}