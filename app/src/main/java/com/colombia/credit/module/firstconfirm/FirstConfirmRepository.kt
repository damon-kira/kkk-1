package com.colombia.credit.module.firstconfirm

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspResult
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class FirstConfirmRepository @Inject constructor() : BaseRepository() {

    fun confirmLoan(bankNo: String, productId: String) = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val jobj = JsonObject()
        jobj.addProperty("s9Cwam", bankNo)
        jobj.addProperty("icRs", productId)
        apiService.confirmLoan(createRequestBody(jobj.toString()))
    }
}