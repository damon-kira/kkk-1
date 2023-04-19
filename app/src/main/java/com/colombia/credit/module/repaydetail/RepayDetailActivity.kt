package com.colombia.credit.module.repaydetail

import android.graphics.Color
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivityRepayDetailBinding
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.showCustomDialog
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
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

    var info: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBarColor(Color.WHITE, true)
        val ids = intent?.getStringExtra(EXTRA_ID)
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }

        mBinding.tvApply.setBlockingOnClickListener {
            toast("调H5支付，暂未处理")
//            Launch.skipWebViewActivity(this, H5UrlManager.URL_PAY)
        }

        mBinding.tvExtension.setBlockingOnClickListener {
            Launch.skipDeferActivity(this, info)
        }

        mViewModel.detailLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.let { data ->
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
            }
        }
        mViewModel.getDetail(ids.orEmpty())
    }
}