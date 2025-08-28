package com.kira.learning.module.history

import com.kira.learning.di.BaseRepository
import com.kira.learning.bean.res.RspHistoryInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class HistoryRepository @Inject constructor(): BaseRepository() {

    fun getHistoryList() = ApiServiceLiveDataProxy.request(RspHistoryInfo::class.java) {
        apiService.getHistoryInfo()
    }
}