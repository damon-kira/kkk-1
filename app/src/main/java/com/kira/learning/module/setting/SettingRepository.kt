package com.kira.learning.module.setting

import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.resp.RspResult
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class SettingRepository @Inject constructor() : BaseRepository() {

    fun logout() = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        apiService.logout(createRequestBody("{}"))
    }
}