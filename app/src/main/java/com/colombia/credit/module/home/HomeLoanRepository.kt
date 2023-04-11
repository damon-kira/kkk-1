package com.colombia.credit.module.home

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspCertProcessInfo
import com.colombia.credit.bean.resp.RspProductInfo
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