package com.kira.learning.module.dashboard.repo

import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import com.kira.learning.app.BaseRepository
import javax.inject.Inject

class RepayCheckRepository @Inject constructor() : BaseRepository() {

    fun checkStatus(loanId: String) = ApiServiceLiveDataProxy.request{
        val jobj = JsonObject()
        jobj.addProperty("ubbsDSOnbw2bas", loanId)
        apiService.checkRepayStatus(createRequestBody(jobj.toString()))
    }
}