package com.kira.learning.module.custom

import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.resp.RspCustom
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class CustomRepository @Inject constructor() : BaseRepository() {

    fun getInfo() = ApiServiceLiveDataProxy.request(RspCustom::class.java) {
        apiService.getCustom()
    }
}