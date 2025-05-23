package com.kira.learning.module.banklist.repo

import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.resp.RspBankAccount
import com.kira.learning.bean.resp.RspBankNameInfo
import com.kira.learning.bean.resp.RspResult
import javax.inject.Inject

class BankCardRepository @Inject constructor() : BaseRepository() {

    fun getBankName() = ApiServiceLiveDataProxy.request(RspBankNameInfo::class.java) {
        apiService.getBankNameList()
    }

    fun getBankAccountList() = ApiServiceLiveDataProxy.request(RspBankAccount::class.java) {
        apiService.getBankAccountList()
    }

    fun updateBank(bankNo: String, productId: String?) =
        ApiServiceLiveDataProxy.request(RspResult::class.java) {
            val jobj = JsonObject()
            jobj.addProperty("TRHkd", bankNo)
            jobj.addProperty("ezYC9Kcci", "")
            jobj.addProperty("LXrtWoarn", productId.orEmpty())
            apiService.updateLoanBank(createRequestBody(jobj.toString()))
        }

}