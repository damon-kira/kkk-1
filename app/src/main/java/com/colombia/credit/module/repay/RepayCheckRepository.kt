package com.colombia.credit.module.repay

import com.colombia.credit.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class RepayCheckRepository @Inject constructor() : BaseRepository() {

    fun checkStatus(loanId: String) = ApiServiceLiveDataProxy.request{
        val jobj = JsonObject()
        jobj.addProperty("ubbsDSOnbw2bas", loanId)
        apiService.checkRepayStatus(createRequestBody(jobj.toString()))
    }
}