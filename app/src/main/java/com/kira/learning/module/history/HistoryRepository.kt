package com.kira.learning.module.history

import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.resp.RspHistoryInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class HistoryRepository @Inject constructor(): BaseRepository() {

    fun getHistoryList() = ApiServiceLiveDataProxy.request(RspHistoryInfo::class.java) {
        apiService.getHistoryInfo()
    }
}