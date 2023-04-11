package com.colombia.credit.module.banklist

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspBankAccount
import com.colombia.credit.bean.resp.RspBankNameInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class BankCardRepository @Inject constructor() : BaseRepository() {

    fun getBankName() = ApiServiceLiveDataProxy.request(RspBankNameInfo::class.java) {
        apiService.getBankNameList()
    }

    fun getBankAccountList() = ApiServiceLiveDataProxy.request(RspBankAccount::class.java) {
        apiService.getBankAccountList()
    }

}