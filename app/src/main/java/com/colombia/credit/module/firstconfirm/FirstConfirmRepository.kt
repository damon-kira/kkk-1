package com.colombia.credit.module.firstconfirm

import com.colombia.credit.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class FirstConfirmRepository @Inject constructor() : BaseRepository() {

    fun confirmLoan(bankNo: String, productId: String) = ApiServiceLiveDataProxy.request {
        val jobj = JsonObject()
        jobj.addProperty("s9Cwam", bankNo)
        jobj.addProperty("icRs", productId)
        apiService.confirmLoan(createRequestBody(jobj.toString()))
    }
}