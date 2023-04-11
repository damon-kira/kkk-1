package com.colombia.credit.module.repeat.confirm

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspRepeatConfirmInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class RepeatConfirmRepository @Inject constructor() : BaseRepository() {

    fun getConfirmInfo(productIds: String) = ApiServiceLiveDataProxy.request(RspRepeatConfirmInfo::class.java) {
        val jobj = JsonObject()
        jobj.addProperty("PXRNZ", productIds)
        apiService.getRepeatConfirmInfo(createRequestBody(jobj.toString()))
    }

}