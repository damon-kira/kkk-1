package com.kira.learning.module.appupdate

import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.resp.AppUpgradeInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject


class AppUpdateRepository @Inject constructor(): BaseRepository() {

    fun getAppUpdate() = ApiServiceLiveDataProxy.request(AppUpgradeInfo::class.java) {
        apiService.getAppUpdate()
    }
}