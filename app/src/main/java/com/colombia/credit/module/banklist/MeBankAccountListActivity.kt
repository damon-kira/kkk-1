package com.colombia.credit.module.banklist

import android.os.Bundle
import com.util.lib.hide
import dagger.hilt.android.AndroidEntryPoint

// 个人中心进入银行账户页面
@AndroidEntryPoint
class MeBankAccountListActivity : BankCardListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.bankCardTvApply.hide()
        mBinding.cardTvSave.hide()
    }
}