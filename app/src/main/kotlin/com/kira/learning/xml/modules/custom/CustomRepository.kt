package com.kira.learning.xml.modules.custom

import com.kira.learning.di.BaseRepository
import com.kira.learning.model.res.RspCustom
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class CustomRepository @Inject constructor() : BaseRepository() {

    fun getInfo() = ApiServiceLiveDataProxy.request(RspCustom::class.java) {
        apiService.getCustom()
    }
}