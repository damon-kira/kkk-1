package com.colombia.credit.module.banklist

import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.module.firstconfirm.ConfirmEvent
import com.colombia.credit.module.firstconfirm.FirstConfirmViewModel
import com.colombia.credit.module.home.HomeEvent
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import dagger.hilt.android.AndroidEntryPoint

// 首付贷确认额度页面进入，需要携带产品id&贷款金额
@AndroidEntryPoint
class ConfirmBankCardListActivity : BankCardListActivity() {

    companion object {
        const val EXTRA_PRODUCT_ID = "product_id"
        const val EXTRA_LOAN_AMOUNT = "loan_amount"
        const val EXTRA_BANK_NO = "bank_no"
    }

    private val mConfirmViewModel by lazyViewModel<FirstConfirmViewModel>()

    private var mProductId: String? = null
    private var mLoanAmount: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val amount = intent.getStringExtra(EXTRA_LOAN_AMOUNT).orEmpty()
        mProductId = intent.getStringExtra(EXTRA_PRODUCT_ID).orEmpty()
        mCurrBankNo = intent.getStringExtra(EXTRA_BANK_NO).orEmpty()
        mLoanAmount = getUnitString(amount)
        setViewModelLoading(mConfirmViewModel)
        mBinding.bankCardTvApply.text = getString(R.string.bank_card_btn_text, mLoanAmount)

        mBinding.bankCardTvApply.setBlockingOnClickListener {
            confirmLoan()
        }

        mConfirmViewModel.confirmLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
                finish()
            } else it.ShowErrorMsg(::confirmLoan)
        }
    }

    override fun updateBank() {
        mViewModel.updateBank(
            bankNo = mAdapter.getSelectData()?.JJ41sQr.orEmpty(),
            productId = mProductId
        )
    }

    private fun confirmLoan() {
        val bankNo = mCurrBankNo
        if (bankNo.isNullOrEmpty() || mProductId.isNullOrEmpty()) {
            LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
            return
        }
        mConfirmViewModel.confirmLoan(bankNo, mProductId.orEmpty())
    }

    override fun updataSuccess() {
        LiveDataBus.post(
            ConfirmEvent(
                ConfirmEvent.EVENT_BANK_NO,
                getSelectBankInfo()?.JJ41sQr.orEmpty()
            )
        )
        super.updataSuccess()
    }
}