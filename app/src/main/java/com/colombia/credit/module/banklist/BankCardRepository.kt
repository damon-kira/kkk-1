package com.colombia.credit.module.banklist

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspBankAccount
import com.colombia.credit.bean.resp.RspBankNameInfo
import com.colombia.credit.bean.resp.RspResult
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
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