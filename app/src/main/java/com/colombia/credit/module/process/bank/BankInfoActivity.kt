package com.colombia.credit.module.process.bank

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqBankInfo
import com.colombia.credit.bean.resp.RspBankInfo
import com.colombia.credit.databinding.ActivityBankInfoBinding
import com.colombia.credit.dialog.BankSearchDialog
import com.colombia.credit.expand.TYPE_IDENTITY
import com.colombia.credit.expand.jumpProcess
import com.colombia.credit.module.banklist.BankCardViewModel
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankInfoActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityBankInfoBinding>()
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
        setToolbarListener(mBinding.processToolbar)
        init()
        lifecycle.addObserver(mBankViewModel)
        setViewModelLoading(mBankViewModel)
    }

    private fun init() {
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
            uploadInfo()
        }
        val cache = mViewModel.getCacheInfo()?.let {info ->
            info as ReqBankInfo
            mBinding.layoutBank.bivName.setViewText(info.thXggvo.orEmpty())
            mBinding.layoutBank.bivName.tag = info.GiQ40BKKr
            mBinding.layoutBank.bivBankno.setViewText(info.Bkmaj97.orEmpty())
            if (info.SElc4 == "0") {
                mBinding.layoutBank.bankRbAhorrs.isChecked = true
            } else if (info.SElc4 == "1") {
                mBinding.layoutBank.bankRbCorriente.isChecked = true
            }
        }
        if (cache == null) {
            mViewModel.getInfo()
        }
    }

    override fun initObserver() {
        mViewModel.mInfoLiveData.observerNonSticky(this) {rspInfo ->
            if (rspInfo !is RspBankInfo) return@observerNonSticky
            rspInfo.hQYeCtjtJh?.let {info ->
                mBinding.layoutBank.bivName.setViewText(info.N61kI40HaH.orEmpty())
                mBinding.layoutBank.bivName.tag = info.TA2B58tdUU
                mBinding.layoutBank.bivBankno.setViewText(info.owuNUS9vAj.orEmpty())
                if (info.`87hVygkzSb` == "0") {
                    mBinding.layoutBank.bankRbAhorrs.isChecked = true
                } else if (info.`87hVygkzSb` == "1") {
                    mBinding.layoutBank.bankRbCorriente.isChecked = true
                }
            }
        }
    }

    override fun checkCommitInfo(): Boolean {
        val bankNoCheck = (mBinding.layoutBank.bivBankno.getViewText().length >= 9).also { result ->
            if (!result) {
                mBinding.layoutBank.bivBankno.setError(R.string.error_bank_no)
            }
        }
        return (mBankType > -1).and(checkAndSetErrorHint(mBinding.layoutBank.bivName)).and(bankNoCheck)
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqBankInfo().also {
            it.thXggvo = mBinding.layoutBank.bivName.getViewText()// 银行名称
            it.Bkmaj97 = mBinding.layoutBank.bivBankno.getViewText()// 银行编码
            it.SElc4 = mBankType.toString()// 银行类型
            it.GiQ40BKKr = mBinding.layoutBank.bivName.tag?.toString().orEmpty() // 银行编码
        }
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel
    override fun getNextType(): Int = TYPE_IDENTITY
}