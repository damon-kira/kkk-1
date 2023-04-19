package com.colombia.credit.module.defer

import android.graphics.Color
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspRepayDetail
import com.colombia.credit.databinding.ActivityDeferBinding
import com.colombia.credit.dialog.ExtensionConfirmDialog
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.showCustomDialog
import com.colombia.credit.expand.toast
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBarColor(Color.WHITE, true)
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }

        getInfo()

        mBinding.tvApply.setBlockingOnClickListener {
            // 调用支付
            ExtensionConfirmDialog(this).setOnClickListener {
                toast("调H5支付，暂未处理")
//                Launch.skipWebViewActivity(this, H5UrlManager.getPayUrl("",""))
            }.show()
        }
    }

    open fun getInfo() {
        intent?.getStringExtra(EXTRA_INFO)?.let {
            val detail = GsonUtil.fromJsonNew<RspRepayDetail.RepayDetail>(it)
            jine = detail?.dbhmAOVp56.orEmpty()
            val amount = getUnitString(detail?.dbhmAOVp56.orEmpty())
            mBinding.tvAmount.text = amount
            mBinding.tvDayValue.text = getString(R.string.days, detail?.zlftJgf ?: "--")
            mBinding.tvDateValue.text = detail?.YXtMfL6nAm
            mBinding.tvRepayAmountValue.text = getUnitString(detail?.EA7nMOa ?: "--")
            mBinding.tvApply.text = getString(R.string.repay_amount_value, amount)
            orderId = detail?.PJpH0.orEmpty()
        }
    }
}