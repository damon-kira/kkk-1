package com.colombia.credit.module.process.bank

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqBankInfo
import com.colombia.credit.bean.resp.BankInfoSearch
import com.colombia.credit.databinding.ActivityBankInfoBinding
import com.colombia.credit.dialog.BankSearchDialog
import com.colombia.credit.expand.TYPE_IDENTITY
import com.colombia.credit.expand.jumpProcess
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class BankInfoActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityBankInfoBinding>()
    private var mBankType = 0

    private val mViewModel by lazyViewModel<BankInfoViewModel>()

    private val mBankDialog: BankSearchDialog by lazy {
        BankSearchDialog(this)
    }

    private val mBankList = arrayListOf<BankInfoSearch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setToolbarListener(mBinding.processToolbar)
        init()
    }

    private fun init() {
        mBinding.bankRbAhorrs.buttonDrawable = StateListDrawable()
        mBinding.bankRbCorriente.buttonDrawable = StateListDrawable()
        mBinding.bankRadiogroup.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.bank_rb_ahorrs -> {
                    mBankType = 0
                }
                R.id.bank_rb_corriente -> {
                    mBankType = 1
                }
            }
        }
        mBinding.bankRbAhorrs.isChecked = true

        mBinding.bivName.setBlockingOnClickListener {
            mBankDialog.setData(mBankList)
                .setOnClickListener {
                    mBinding.bivName.setViewText(it.getBankName().orEmpty())
                    mBinding.bivName.tag = it.BCode
                }.show()
        }
        mBinding.tvCommit.setBlockingOnClickListener {
            uploadInfo()
        }
    }

    override fun checkCommitInfo(): Boolean {
        val bankNoCheck = (mBinding.bivBankno.getViewText().length >= 9).also { result ->
            if (!result) {
                mBinding.bivBankno.setError(R.string.error_bank_no)
            }
        }
        return (mBankType > -1).and(checkAndSetErrorHint(mBinding.bivName)).and(bankNoCheck)
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqBankInfo().also {
            it.dmQHAqbC = mBinding.bivName.getViewText()// 银行名称
            it.Wf3pgbaTm = mBinding.bivBankno.getViewText()// 银行编码
            it.OZwuBMo = mBankType.toString()// 银行类型
            it.COZthiVwS = mBinding.bivName.tag?.toString().orEmpty() // 银行编码
        }
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel

    override fun uploadSuccess() {
        jumpProcess(this, TYPE_IDENTITY)
    }
}