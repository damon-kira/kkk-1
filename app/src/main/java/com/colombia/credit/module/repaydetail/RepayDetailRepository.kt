package com.colombia.credit.module.repaydetail

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspRepayDetail
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class RepayDetailRepository @Inject constructor() : BaseRepository() {

    fun getDetail(ids: String) = ApiServiceLiveDataProxy.request(RspRepayDetail::class.java) {
        val jobj = JsonObject()
        jobj.addProperty("D1R1GfjXG", ids)
        apiService.getRepayDetail(createRequestBody(jobj.toString()))
    }
}