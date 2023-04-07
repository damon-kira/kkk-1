package com.colombia.credit.module.appupdate

import com.colombia.credit.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject


class AppUpdateRepository @Inject constructor(): BaseRepository() {

    fun getAppUpdate() = ApiServiceLiveDataProxy.request {
        apiService.getAppUpdate()
    }
}