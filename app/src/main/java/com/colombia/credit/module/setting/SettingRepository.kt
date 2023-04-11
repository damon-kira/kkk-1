package com.colombia.credit.module.setting

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspResult
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class SettingRepository @Inject constructor() : BaseRepository() {

    fun logout() = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        apiService.logout(createRequestBody("{}"))
    }
}