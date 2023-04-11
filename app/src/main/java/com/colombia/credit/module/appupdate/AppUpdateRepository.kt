package com.colombia.credit.module.appupdate

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.AppUpgradeInfo
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject


class AppUpdateRepository @Inject constructor(): BaseRepository() {

    fun getAppUpdate() = ApiServiceLiveDataProxy.request(AppUpgradeInfo::class.java) {
        apiService.getAppUpdate()
    }
}