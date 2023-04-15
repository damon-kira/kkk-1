package com.colombia.credit.module.custom

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspCustom
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class CustomRepository @Inject constructor() : BaseRepository() {

    fun getInfo() = ApiServiceLiveDataProxy.request(RspCustom::class.java) {
        apiService.getCustom()
    }
}