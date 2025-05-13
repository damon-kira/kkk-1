package com.kira.learning.module.home

import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.resp.RspCertProcessInfo
import com.kira.learning.bean.resp.RspProductInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class HomeLoanRepository @Inject constructor(): BaseRepository() {

    fun getHomeInfo() = ApiServiceLiveDataProxy.request(RspProductInfo::class.java) {
        apiService.getHomeInfo()
    }

    fun getCertProcess() = ApiServiceLiveDataProxy.request(RspCertProcessInfo::class.java) {
        apiService.getCertProcess()
    }
}