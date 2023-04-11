package com.colombia.credit.module.banklist

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqBankInfo
import com.colombia.credit.databinding.ActivityBankinfoAddBinding
import com.colombia.credit.dialog.BankSearchDialog
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.module.process.bank.BankInfoViewModel
import com.colombia.credit.view.baseinfo.AbsBaseInfoView
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

// 添加银行卡信息
@AndroidEntryPoint
class BankInfoAddActivity : BaseActivity() {

    private val mBinding by binding<ActivityBankinfoAddBinding>()

    private var mBankType = 0

    private val mViewModel by lazyViewModel<BankInfoViewModel>()

    private val mBankViewModel by lazyViewModel<BankCardViewModel>()

    private val mBankDialog: BankSearchDialog by lazy {
        BankSearchDialog(this)
            .setData(mBankViewModel.mRspBankNameList ?: arrayListOf())
            .setOnSelectListener {
                mBinding.layoutBank.bivName.setViewText(it.KoGUgumBVm.orEmpty())
                mBinding.layoutBank.bivName.tag = it.Vu3XbBF
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setViewModelLoading(mViewModel)
        setViewModelLoading(mBankViewModel)
        lifecycle.addObserver(mBankViewModel)
        mBinding.layoutBank.tvSafe.hide()
        mBinding.layoutBank.tvCommit.setText(R.string.confirm)
        mBinding.layoutBank.bivBanknoTow.show()
        mBinding.layoutBank.bivBankno.showDesc(false)

        mBinding.layoutBank.bankRbAhorrs.buttonDrawable = StateListDrawable()
        mBinding.layoutBank.bankRbCorriente.buttonDrawable = StateListDrawable()
        mBinding.layoutBank.bankRadiogroup.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.bank_rb_ahorrs -> {
                    mBankType = 0
                }
                R.id.bank_rb_corriente -> {
                    mBankType = 1
                }
            }
        }
        mBinding.layoutBank.bankRbAhorrs.isChecked = true

        mBinding.layoutBank.bivName.setBlockingOnClickListener {
            mBankViewModel.mRspBankNameList?.let {
                mBankDialog.show()
            }
        }
        mBinding.layoutBank.tvCommit.setBlockingOnClickListener {
            if (checkCommitInfo()) {
                mViewModel.uploadInfo(getCommitInfo())
            }
        }

        mViewModel.mUploadLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                finish()
            } else it.ShowErrorMsg()
        }
    }

    private fun checkCommitInfo(): Boolean {
        val bankNo = mBinding.layoutBank.bivBankno.getViewText()
        val bankNoCheck = (bankNo.length >= 9).also { result ->
            if (!result) {
                mBinding.layoutBank.bivBankno.setError(R.string.error_bank_no)
            }
        }

        val bankNoConfirm = mBinding.layoutBank.bivBanknoTow.getViewText()
        val bankNoTwoCheck = if (bankNoConfirm.isEmpty()) {
            checkAndSetErrorHint(mBinding.layoutBank.bivBanknoTow, mBinding.layoutBank.bivBanknoTow.getTitle())
            false
        } else if(bankNo != bankNoConfirm) {
            mBinding.layoutBank.bivBanknoTow.setError(R.string.error_bank_no)
            false
        } else true
        bankNoCheck.and(bankNoTwoCheck)
        return (mBankType > -1).and(checkAndSetErrorHint(mBinding.layoutBank.bivName))
            .and(bankNoCheck)
    }

    private fun getCommitInfo(): IReqBaseInfo {
        return ReqBankInfo().also {
            it.thXggvo = mBinding.layoutBank.bivName.getViewText()// 银行名称
            it.Bkmaj97 = mBinding.layoutBank.bivBankno.getViewText()// 银行编码
            it.SElc4 = mBankType.toString()// 银行类型
            it.GiQ40BKKr = mBinding.layoutBank.bivName.tag?.toString().orEmpty() // 银行编码
            it.cOzMNSKThS = "09"
        }
    }

    private fun checkAndSetErrorHint(
        baseInfoView: AbsBaseInfoView,
        errorHint: String? = null
    ): Boolean {
        val text = baseInfoView.getViewText()
        var result = true
        if (text.isEmpty()) {
            result = false
            if (errorHint.isNullOrEmpty()) {
                baseInfoView.setError(
                    getString(
                        R.string.error_process_hint,
                        baseInfoView.getTitle()
                    )
                )
            } else {
                baseInfoView.setError(getString(R.string.error_process_hint, errorHint))
            }
        }
        return result
    }
}