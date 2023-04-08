package com.colombia.credit.module.home

import com.colombia.credit.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class HomeLoanRepository @Inject constructor(): BaseRepository() {

    fun getHomeInfo() = ApiServiceLiveDataProxy.request {
        apiService.getHomeInfo()
    }
}