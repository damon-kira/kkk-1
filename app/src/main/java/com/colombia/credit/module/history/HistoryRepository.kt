package com.colombia.credit.module.history

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspHistoryInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class HistoryRepository @Inject constructor(): BaseRepository() {

    fun getHistoryList() = ApiServiceLiveDataProxy.request(RspHistoryInfo::class.java) {
        apiService.getHistoryInfo()
    }
}