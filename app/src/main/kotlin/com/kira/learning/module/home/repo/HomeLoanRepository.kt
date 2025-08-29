package com.kira.learning.module.home.repo

import com.common.lib.net.ApiServiceLiveDataProxy
import com.kira.learning.di.BaseRepository
import com.kira.learning.bean.res.RspCertProcessInfo
import com.kira.learning.bean.res.RspProductInfo
import javax.inject.Inject

class HomeLoanRepository @Inject constructor(): BaseRepository() {

    fun getHomeInfo() = ApiServiceLiveDataProxy.request(RspProductInfo::class.java) {
        apiService.getHomeInfo()
    }

    fun getCertProcess() = ApiServiceLiveDataProxy.request(RspCertProcessInfo::class.java) {
        apiService.getCertProcess()
    }
}