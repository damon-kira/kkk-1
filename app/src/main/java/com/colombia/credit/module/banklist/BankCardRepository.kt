package com.colombia.credit.module.banklist

import com.colombia.credit.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class BankCardRepository @Inject constructor() : BaseRepository() {

    fun getBankName() = ApiServiceLiveDataProxy.request {
        apiService.getBankNameList()
    }

    fun getBankAccountList() = ApiServiceLiveDataProxy.request {
        apiService.getBankAccountList()
    }

}