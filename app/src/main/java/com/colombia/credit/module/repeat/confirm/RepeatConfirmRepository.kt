package com.colombia.credit.module.repeat.confirm

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspRepeatCalcul
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class RepeatConfirmRepository @Inject constructor() : BaseRepository() {

    // 复贷产品首页选择产品后使用
    fun getConfirmInfo(productIds: String, orderId: String) =
        ApiServiceLiveDataProxy.request(RspRepeatCalcul::class.java) {
            val jobj = JsonObject()
            jobj.addProperty("PXRNZ", productIds)
            jobj.addProperty("AGdAay7dc5", orderId)
            apiService.getRepeatCalcul(createRequestBody(jobj.toString()))
        }
}