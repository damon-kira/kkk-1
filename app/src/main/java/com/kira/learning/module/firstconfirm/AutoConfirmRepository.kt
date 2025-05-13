package com.kira.learning.module.firstconfirm

import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.resp.RspResult
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class AutoConfirmRepository @Inject constructor() : BaseRepository() {

    fun getTimeMill(id1: String, productId: String) =
        ApiServiceLiveDataProxy.request(Long::class.java) {
            val jobj = JsonObject()
            jobj.addProperty("aLHWU", id1)
            jobj.addProperty("iTJVkff", productId)
            apiService.getCountdownTime(createRequestBody(jobj.toString()))
        }

    fun cancel(id1: String, productId: String) =
        ApiServiceLiveDataProxy.request(RspResult::class.java) {
            val jobj = JsonObject()
            jobj.addProperty("AGmZU", id1)
            jobj.addProperty("sxgiG", productId)
            apiService.cancelAuto(createRequestBody(jobj.toString()))
        }
}